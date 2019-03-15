DIR=$(shell pwd)

#
# Java
#

JAVA_DIR=${JAVA_HOME}
JAVA_LIB=

# JNI

JNI_LIB=libCrypto.jnilib
JNI_SDIR=Crypto/src/main/cpp/jni
JNI_SRCS=
JNI_OBJS=$(JNI_SRCS:.c=.o)
JNI_HDRS=$(JNI_SRCS:.c=.h)

# Crypto Api

JAVA_API_SDIR=Crypto/src/main/java/com/breadwallet/crypto/api
JAVA_API_SRCS=\
	$(JAVA_API_SDIR)/bitcoin/BitcoinBackendClient.java \
	$(JAVA_API_SDIR)/bitcoin/BitcoinChainParams.java \
	$(JAVA_API_SDIR)/bitcoin/BitcoinMasterPubKey.java \
	$(JAVA_API_SDIR)/bitcoin/BitcoinPersistenceClient.java \
	$(JAVA_API_SDIR)/bitcoin/BitcoinWalletManagerListener.java \
	$(JAVA_API_SDIR)/events/transfer/TransferEvent.java \
	$(JAVA_API_SDIR)/events/wallet/WalletEvent.java \
	$(JAVA_API_SDIR)/events/wallet/BalanceUpdatedWalletEvent.java \
	$(JAVA_API_SDIR)/events/wallet/CreatedWalletEvent.java \
	$(JAVA_API_SDIR)/events/wallet/DeletedWalletEvent.java \
	$(JAVA_API_SDIR)/events/walletmanager/WalletManagerEvent.java \
	$(JAVA_API_SDIR)/events/walletmanager/ChangedWalletManagerEvent.java \
	$(JAVA_API_SDIR)/events/walletmanager/SyncStartedWalletManagerEvent.java \
	$(JAVA_API_SDIR)/events/walletmanager/SyncEndedWalletManagerEvent.java \
	$(JAVA_API_SDIR)/factories/AccountFactory.java \
	$(JAVA_API_SDIR)/factories/NetworkFactory.java \
	$(JAVA_API_SDIR)/factories/WalletManagerFactory.java \
	$(JAVA_API_SDIR)/walletmanager/WalletManagerBackendClient.java \
	$(JAVA_API_SDIR)/walletmanager/WalletManagerListener.java \
	$(JAVA_API_SDIR)/walletmanager/WalletManagerPersistenceClient.java \
	$(JAVA_API_SDIR)/Account.java \
	$(JAVA_API_SDIR)/Address.java \
	$(JAVA_API_SDIR)/Amount.java \
	$(JAVA_API_SDIR)/Bitcoin.java \
	$(JAVA_API_SDIR)/CryptoApi.java \
	$(JAVA_API_SDIR)/Currency.java \
	$(JAVA_API_SDIR)/Network.java \
	$(JAVA_API_SDIR)/Transfer.java \
	$(JAVA_API_SDIR)/Unit.java \
	$(JAVA_API_SDIR)/Wallet.java \
	$(JAVA_API_SDIR)/WalletManager.java
JAVA_API_OBJS=$(JAVA_API_SRCS:.java=.class)

# Crypto Core

JAVA_CORE_SDIR=Crypto/src/main/java/com/breadwallet/crypto/core
JAVA_CORE_SRCS=\
	$(JAVA_CORE_SDIR)/bitcoin/jni/CoreBitcoinChainParams.java \
	$(JAVA_CORE_SDIR)/bitcoin/jni/CoreBitcoinMasterPubKey.java \
	$(JAVA_CORE_SDIR)/bitcoin/jni/CoreBitcoinWallet.java \
	$(JAVA_CORE_SDIR)/bitcoin/jni/CoreBitcoinWalletManager.java \
	$(JAVA_CORE_SDIR)/bitcoin/jni/CoreBitcoinWalletManagerClient.java \
	$(JAVA_CORE_SDIR)/bitcoin/BitcoinChainParams.java \
	$(JAVA_CORE_SDIR)/bitcoin/BitcoinChainParamsAdapter.java \
	$(JAVA_CORE_SDIR)/bitcoin/BitcoinMasterPubKey.java \
	$(JAVA_CORE_SDIR)/bitcoin/BitcoinMasterPubKeyAdapter.java \
	$(JAVA_CORE_SDIR)/bitcoin/BitcoinWallet.java \
	$(JAVA_CORE_SDIR)/bitcoin/BitcoinWalletManager.java \
	$(JAVA_CORE_SDIR)/common/jni/JniReference.java \
	$(JAVA_CORE_SDIR)/jni/Bip39.java \
	$(JAVA_CORE_SDIR)/Account.java \
	$(JAVA_CORE_SDIR)/CoreCryptoApi.java
JAVA_CORE_OBJS=$(JAVA_CORE_SRCS:.java=.class)

#
# Core
#

# Bitcoin

C_SDIR=Crypto/src/main/cpp/core

CORE_SRCS=
CORE_OBJS=$(CORE_SRCS:.c=.o)

# Ethereum

ETH_SRCS=
ETH_OBJS=$(ETH_SRCS:.c=.o)

#
# Targets
#

compile: $(JNI_LIB) java_comp

$(JNI_LIB): $(JNI_OBJS) $(CORE_OBJS)
	cc -dynamiclib -o $(JNI_LIB) $(JNI_OBJS) $(CORE_OBJS)

java_comp:	FORCE
	@mkdir -p build
	@echo "Crypto Java"
	@javac -d build $(JAVA_API_SRCS) $(JAVA_CORE_SRCS)

jni_hdr_crypto:	FORCE
	@echo Crypto JNI Headers
	@(cd build/com/breadwallet/crypto/core/bitcoin/jni; \
	  for class in *.class; do \
	      javah -jni -d $(DIR)/$(JNI_SDIR) -classpath $(DIR)/build com.breadwallet.crypto.core.bitcoin.jni.$${class%%.class}; \
	  done)
	@(cd build/com/breadwallet/crypto/core/common/jni; \
	  for class in *.class; do \
	      javah -jni -d $(DIR)/$(JNI_SDIR) -classpath $(DIR)/build com.breadwallet.crypto.core.common.jni.$${class%%.class}; \
	  done)
	@(cd build/com/breadwallet/crypto/core/jni; \
	  for class in *.class; do \
	      javah -jni -d $(DIR)/$(JNI_SDIR) -classpath $(DIR)/build com.breadwallet.crypto.core.jni.$${class%%.class}; \
	  done)
	@(cd $(JNI_SDIR); \
	  rm -f .h \
			com_breadwallet_crypto_core_bitcoin_jni_CoreBitcoinWallet.h \
			com_breadwallet_crypto_core_bitcoin_jni_CoreBitcoinWalletManagerClient.h)


jni_hdr: java_comp jni_hdr_crypto

clean:
	rm -rf build $(JNI_OBJS) $(CORE_OBJS) $(JAVA_API_OBJS) $(JAVA_CORE_OBJS) $(JNI_LIB)

FORCE:

# test: $(JNI_LIB) java_comp
# 	java -Xss1m -Dwallet.test -classpath build -Djava.library.path=. \
# 		 com.breadwallet.core.test.BRWalletManager $(ARGS) # -D.

# debug: $(JNI_LIB) java_comp
# 	java -Xss1m -Xdebug -Xrunjdwp:transport=dt_socket,address=8008,server=y,suspend=n \
# 		 -Dwallet.test -classpath build -Djava.library.path=. \
# 		 com.breadwallet.core.test.BRWalletManager $(ARGS) # -D.
