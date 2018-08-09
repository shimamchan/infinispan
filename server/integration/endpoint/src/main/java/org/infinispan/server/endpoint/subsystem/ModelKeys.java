/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011-2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.infinispan.server.endpoint.subsystem;

/**
 * @author Tristan Tarrant
 */
public class ModelKeys {

   public static final String HOTROD_CONNECTOR = "hotrod-connector";
   public static final String MEMCACHED_CONNECTOR = "memcached-connector";
   public static final String REST_CONNECTOR = "rest-connector";
   public static final String ROUTER_CONNECTOR = "router-connector";
   @Deprecated
   public static final String WEBSOCKET_CONNECTOR = "websocket-connector";

   public static final String NAME = "name"; // string
   public static final String CACHE_NAME = "cache-name"; // string
   public static final String SOCKET_BINDING = "socket-binding"; // string
   public static final String HOTROD_SOCKET_BINDING = "hotrod-socket-binding"; // string
   public static final String REST_SOCKET_BINDING = "rest-socket-binding"; // string
   public static final String SINGLE_PORT_SOCKET_BINDING = "single-port-socket-binding";
   @Deprecated
   public static final String KEEP_ALIVE = "keep-alive"; // boolean
   public static final String CACHE = "cache"; // string
   public static final String CACHE_CONTAINER = "cache-container"; // string
   public static final String IO_THREADS = "io-threads"; // integer
   public static final String WORKER_THREADS = "worker-threads"; // integer
   public static final String IDLE_TIMEOUT = "idle-timeout"; // integer
   public static final String TCP_NODELAY = "tcp-nodelay"; // boolean
   public static final String TCP_KEEPALIVE = "tcp-keepalive"; // boolean
   public static final String SEND_BUFFER_SIZE = "send-buffer-size"; // integer
   public static final String RECEIVE_BUFFER_SIZE = "receive-buffer-size"; // integer
   public static final String VIRTUAL_HOST = "virtual-host"; // string
   public static final String VIRTUAL_SERVER = "virtual-server"; // string
   public static final String CONTEXT_PATH = "context-path"; // string
   public static final String REQUIRE_SSL_CLIENT_AUTH = "require-ssl-client-auth"; // boolean
   public static final String SECURITY_DOMAIN = "security-domain"; // string
   public static final String SECURITY_REALM = "security-realm"; // string
   public static final String AUTH_METHOD = "auth-method"; // string
   public static final String SECURITY_MODE = "security-mode"; // string
   public static final String EXTENDED_HEADERS = "extended-headers"; //enum
   public static final String MAX_CONTENT_LENGTH = "max-content-length"; //int
   public static final String MAX_AGE_SECONDS = "max-age-seconds"; //int
   public static final String COMPRESSION_LEVEL = "compression-level"; //int

   public static final String TOPOLOGY_STATE_TRANSFER_NAME = "TOPOLOGY_STATE_TRANSFER";
   public static final String TOPOLOGY_STATE_TRANSFER = "topology-state-transfer";
   public static final String LOCK_TIMEOUT = "lock-timeout"; // integer
   public static final String REPLICATION_TIMEOUT = "replication-timeout"; // integer
   public static final String UPDATE_TIMEOUT = "update-timeout"; // integer
   public static final String EXTERNAL_HOST = "external-host"; // string
   public static final String EXTERNAL_PORT = "external-port"; // integer
   public static final String LAZY_RETRIEVAL = "lazy-retrieval"; // boolean
   public static final String AWAIT_INITIAL_RETRIEVAL = "await-initial-retrieval"; // boolean
   @Deprecated
   public static final String CACHE_SUFFIX = "cache-suffix";

   public static final String AUTHENTICATION_NAME = "AUTHENTICATION";
   public static final String AUTHENTICATION = "authentication";
   public static final String ALLOW_CREDENTIALS = "allow-credentials";
   public static final String ALLOWED_ORIGINS = "allowed-origins";
   public static final String ALLOWED_METHODS = "allowed-methods";
   public static final String ALLOWED_HEADERS = "allowed-headers";
   public static final String CORS_RULES = "cors-rules";
   public static final String CORS_RULE = "cors-rule";
   public static final String ENCRYPTION_NAME = "ENCRYPTION";
   public static final String EXPOSE_HEADERS = "expose-headers";
   public static final String ENCRYPTION = "encryption";
   public static final String SNI = "sni";
   public static final String PREFIX = "prefix";
   public static final String HOST_NAME = "host-name";
   public static final String PATH = "path";
   public static final String SASL_NAME = "SASL";
   public static final String SASL = "sasl";
   public static final String SASL_POLICY_NAME = "SASL_POLICY";
   public static final String SASL_POLICY = "sasl-policy";
   public static final String SSL = "ssl"; // boolean
   @Deprecated
   public static final String SECURITY = "security";
   public static final String SERVER_CONTEXT_NAME = "server-context-name";
   public static final String SERVER_NAME = "server-name";
   public static final String MECHANISMS = "mechanisms";
   public static final String POLICY = "policy";
   public static final String PROPERTY = "property";
   public static final String QOP = "qop";
   public static final String STRENGTH = "strength";
   public static final String VALUE = "value";

   public static final String FORWARD_SECRECY = "forward-secrecy";
   public static final String NO_ACTIVE = "no-active";
   public static final String NO_ANONYMOUS = "no-anonymous";
   public static final String NO_DICTIONARY = "no-dictionary";
   public static final String NO_PLAIN_TEXT = "no-plain-text";
   public static final String PASS_CREDENTIALS = "pass-credentials";
   public static final String IGNORED_CACHE = "ignored-cache";
   public static final String IGNORED_CACHES = "ignored-caches";
   public static final String CACHE_NAMES = "cache-names";

   public static final String HOTROD = "hotrod";
   public static final String REST = "rest";
   public static final String MULTI_TENANCY = "multi-tenancy";
   public static final String MULTI_TENANCY_NAME = "MULTI_TENANCY";

   public static final String MANAGEMENT_HTTP = "management-http";
   public static final String MANAGEMENT_HTTPS = "management-https";


   public static final String SINGLE_PORT = "single-port";
   public static final String SINGLE_PORT_NAME = "SINGLE_PORT";
}
