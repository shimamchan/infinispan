package org.infinispan.query.indexmanager;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import org.hibernate.search.backend.FlushLuceneWork;
import org.hibernate.search.backend.IndexingMonitor;
import org.hibernate.search.backend.LuceneWork;
import org.hibernate.search.backend.PurgeAllLuceneWork;
import org.hibernate.search.indexes.spi.IndexManager;
import org.infinispan.commands.ReplicableCommand;
import org.infinispan.commons.util.Util;
import org.infinispan.query.logging.Log;
import org.infinispan.remoting.inboundhandler.DeliverOrder;
import org.infinispan.remoting.rpc.RpcManager;
import org.infinispan.remoting.transport.Address;
import org.infinispan.remoting.transport.impl.VoidResponseCollector;
import org.infinispan.util.ByteString;
import org.infinispan.util.logging.LogFactory;

/**
 * The IndexingBackend which forwards all operations to a different node
 * using Infinispan's custom commands.
 *
 * @author Sanne Grinovero &lt;sanne@hibernate.org&gt; (C) 2014 Red Hat Inc.
 * @since 7.0
 */
final class RemoteIndexingBackend implements IndexingBackend {

   private static final Log log = LogFactory.getLog(RemoteIndexingBackend.class, Log.class);

   private final int GRACE_MILLISECONDS_FOR_REPLACEMENT = 4000;
   private final int POLLING_MILLISECONDS_FOR_REPLACEMENT = 4000;
   protected static final Set<Class<? extends LuceneWork>> SYNC_ONLY_WORKS =
         Util.asSet(PurgeAllLuceneWork.class, FlushLuceneWork.class);

   private final String cacheName;
   private final String indexName;
   private final Address recipient;
   private final RpcManager rpcManager;
   private final Address masterAddress;
   private final boolean async;

   private volatile IndexingBackend replacement;

   public RemoteIndexingBackend(String cacheName, RpcManager rpcManager, String indexName, Address masterAddress, boolean async) {
      this.cacheName = cacheName;
      this.rpcManager = rpcManager;
      this.indexName = indexName;
      this.masterAddress = masterAddress;
      this.recipient = masterAddress;
      this.async = async;
   }

   @Override
   public void flushAndClose(IndexingBackend replacement) {
      if (replacement != null) {
         this.replacement = replacement;
      }
   }

   @Override
   public void applyWork(List<LuceneWork> workList, IndexingMonitor monitor, IndexManager indexManager) {
      IndexUpdateCommand command = new IndexUpdateCommand(ByteString.fromString(cacheName));
      //Use Search's custom Avro based serializer as it includes support for back/future compatibility
      byte[] serializedModel = indexManager.getSerializer().toSerializedModel(workList);
      command.setSerializedWorkList(serializedModel);
      command.setIndexName(this.indexName);
      try {
         log.applyingChangeListRemotely(workList);
         sendCommand(command, workList, shouldSendSync(workList));
      } catch (Exception e) {
         waitForReplacementBackend();
         if (replacement != null) {
            replacement.applyWork(workList, monitor, indexManager);
         } else {
            throw e;
         }
      }
   }

   /**
    * Decides whether a command should be sent sync or async
    */
   private boolean shouldSendSync(LuceneWork operation) {
      return !async || SYNC_ONLY_WORKS.contains(operation.getClass());
   }

   private boolean shouldSendSync(List<LuceneWork> operations) {
      return !async || (operations.size() == 1 && shouldSendSync(operations.get(0)));
   }


   @Override
   public void applyStreamWork(LuceneWork singleOperation, IndexingMonitor monitor, IndexManager indexManager) {
      final IndexUpdateStreamCommand streamCommand = new IndexUpdateStreamCommand(ByteString.fromString(cacheName));
      final List<LuceneWork> operations = Collections.singletonList(singleOperation);
      final byte[] serializedModel = indexManager.getSerializer().toSerializedModel(operations);
      streamCommand.setSerializedWorkList(serializedModel);
      streamCommand.setIndexName(this.indexName);
      try {
         sendCommand(streamCommand, operations, shouldSendSync(singleOperation));
      } catch (Exception e) {
         waitForReplacementBackend();
         if (replacement != null) {
            replacement.applyStreamWork(singleOperation, monitor, indexManager);
         } else {
            throw e;
         }
      }
   }

   /**
    * If some error happened, and a new IndexingBackend was provided, it is safe to
    * assume the error relates with the fact the current backend is no longer valid.
    * At this point we can still forward the work from the current stack to the next
    * backend, creating a linked list of forwards to the right backend:
    */
   private void waitForReplacementBackend() {
      int waitedMilliseconds = 0;
      try {
         while (replacement != null) {
            if (waitedMilliseconds >= GRACE_MILLISECONDS_FOR_REPLACEMENT) {
               return;
            }
            Thread.sleep(POLLING_MILLISECONDS_FOR_REPLACEMENT);
            waitedMilliseconds += POLLING_MILLISECONDS_FOR_REPLACEMENT;
         }
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
      }
   }

   private void sendCommand(ReplicableCommand command, List<LuceneWork> workList, boolean sync) {
      if (sync) {
         CompletionStage<Void> completionStage = rpcManager.invokeCommand(recipient, command, VoidResponseCollector.ignoreLeavers(),
               rpcManager.getSyncRpcOptions());
         rpcManager.blocking(completionStage);
      } else {
         rpcManager.sendTo(recipient, command, DeliverOrder.PER_SENDER);
      }
      log.workListRemotedTo(workList, masterAddress);
   }

   @Override
   public boolean isMasterLocal() {
      return false;
   }

   public String toString() {
      return "RemoteIndexingBackend(" + masterAddress + ")";
   }

}
