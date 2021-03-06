package org.infinispan.commands.remote.recovery;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.transaction.xa.Xid;

import org.infinispan.commands.remote.BaseRpcCommand;
import org.infinispan.factories.ComponentRegistry;
import org.infinispan.transaction.xa.recovery.RecoveryManager;
import org.infinispan.util.ByteString;

/**
 * Command used by the recovery tooling for forcing transaction completion .
 *
 * @author Mircea Markus
 * @since 5.0
 */
public class CompleteTransactionCommand extends BaseRpcCommand {

   public static final byte COMMAND_ID = 24;

   /**
    * The tx which we want to complete.
    */
   private Xid xid;

   /**
    * if true the transaction is committed, otherwise it is rolled back.
    */
   private boolean commit;

   private CompleteTransactionCommand() {
      super(null); // For command id uniqueness test
   }

   public CompleteTransactionCommand(ByteString cacheName) {
      super(cacheName);
   }

   public CompleteTransactionCommand(ByteString cacheName, Xid xid, boolean commit) {
      super(cacheName);
      this.xid = xid;
      this.commit = commit;
   }

   @Override
   public CompletionStage<?> invokeAsync(ComponentRegistry componentRegistry) throws Throwable {
      RecoveryManager recoveryManager = componentRegistry.getRecoveryManager().running();
      return CompletableFuture.completedFuture(recoveryManager.forceTransactionCompletion(xid, commit));
   }

   @Override
   public boolean isReturnValueExpected() {
      return true;
   }

   @Override
   public byte getCommandId() {
      return COMMAND_ID;
   }

   @Override
   public void writeTo(ObjectOutput output) throws IOException {
      output.writeObject(xid);
      output.writeBoolean(commit);
   }

   @Override
   public void readFrom(ObjectInput input) throws IOException, ClassNotFoundException {
      xid = (Xid) input.readObject();
      commit = input.readBoolean();
   }

   @Override
   public boolean canBlock() {
      //this command performs the 2PC commit.
      return true;
   }

   @Override
   public String toString() {
      return getClass().getSimpleName() +
            "{ xid=" + xid +
            ", commit=" + commit +
            ", cacheName=" + cacheName +
            "} ";
   }
}
