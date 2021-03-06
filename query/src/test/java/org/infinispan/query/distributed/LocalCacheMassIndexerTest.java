package org.infinispan.query.distributed;

import static org.infinispan.query.dsl.IndexedQueryMode.FETCH;
import static org.testng.AssertJUnit.assertEquals;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.context.Flag;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.query.CacheQuery;
import org.infinispan.query.Search;
import org.infinispan.query.SearchManager;
import org.infinispan.query.queries.faceting.Car;
import org.infinispan.query.test.Person;
import org.infinispan.test.SingleCacheManagerTest;
import org.infinispan.test.fwk.TestCacheManagerFactory;
import org.testng.annotations.Test;

/**
 * Tests for Mass Indexer in a local cache.
 *
 * @author gustavonalle
 * @since 7.1
 */

@Test(groups = "functional", testName = "query.distributed.LocalCacheMassIndexerTest")
public class LocalCacheMassIndexerTest extends SingleCacheManagerTest {

   private static final int NUM_ENTITIES = 2000;

   @Override
   protected EmbeddedCacheManager createCacheManager() throws Exception {
      ConfigurationBuilder cfg = getDefaultStandaloneCacheConfig(false);
      cfg.indexing().enable()
            .addIndexedEntity(Person.class)
            .addProperty("default.directory_provider", "local-heap")
            .addProperty("lucene_version", "LUCENE_CURRENT");
      return TestCacheManagerFactory.createCacheManager(cfg);
   }

   private int indexSize(Cache<?, ?> cache) {
      SearchManager searchManager = Search.getSearchManager(cache);
      CacheQuery<Person> query = searchManager.getQuery("FROM " + Person.class.getName(), FETCH);
      return query.getResultSize();
   }

   @Test
   public void testMassIndexer() {
      for (int i = 0; i < NUM_ENTITIES; i++) {
         cache.put(i, new Person("name" + i, "blurb" + i, i));
      }
      SearchManager searchManager = Search.getSearchManager(cache);

      assertEquals(NUM_ENTITIES, indexSize(cache));

      searchManager.getMassIndexer().start();
      assertEquals(NUM_ENTITIES, indexSize(cache));

      cache.clear();
      searchManager.getMassIndexer().start();

      assertEquals(0, indexSize(cache));
   }

   public void testPartiallyReindex() throws Exception {
      cache.getAdvancedCache().withFlags(Flag.SKIP_INDEXING).put(0, new Person("name" + 0, "blurb" + 0, 0));
      verifyFindsPerson(0, "name" + 0);
      Search.getSearchManager(cache).getMassIndexer().reindex(0).get();
      verifyFindsPerson(1, "name" + 0);
      cache.remove(0);
      verifyFindsPerson(0, "name" + 0);
   }

   protected void verifyFindsPerson(int expectedCount, String name) {
      SearchManager searchManager = Search.getSearchManager(cache);
      String q = String.format("FROM %s where name:'%s'", Person.class.getName(), name);
      CacheQuery<Car> cacheQuery = searchManager.getQuery(q, FETCH);
      assertEquals(expectedCount, cacheQuery.getResultSize());
   }
}
