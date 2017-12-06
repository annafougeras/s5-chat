# Dossiers
SRC = src
BIN = bin
DOC = doc
JAVADOC = $(DOC)/javadoc

# Packages nécessaires
PACKAGES_CLIENT = 
PACKAGES_ADMIN = 
PACKAGES_SERVEUR = 
PACKAGES_TESTS = tests communication communication.simple commChatS5 modele

# Liste des packages utiles (sort trie et enlève les doublons)
PACKAGES_TOUT = $(sort $(PACKAGES_CLIENT) $(PACKAGES_ADMIN) $(PACKAGES_SERVEUR) $(PACKAGES_TESTS))









# Compile le java (.java -> .class)
java: | $(BIN)
	javac -sourcepath $(SRC) -d $(BIN) $(foreach package, $(PACKAGES_TOUT), $(SRC)/$(subst .,/,$(package))/*.java)


# Installe la base de donnée (cf. makefile du dossier db/)
install_db: 
	make -C db install






# Javadoc
doc: | $(JAVADOC)
	javadoc -use -quiet -public $(PACKAGES_TOUT) -sourcepath $(SRC) -d $(JAVADOC) -charset "UTF-8"


test_communication:
	xterm -ge 80x15+0+000 -e "java -classpath $(BIN) tests.TestComSimpleServeur 8888" &
	xterm -ge 80x15+0+230 -e "sleep 1 && java -classpath $(BIN) tests.TestComSimpleClient 127.0.0.2 8888" &
	xterm -ge 80x15+0+460 -e "sleep 3 && java -classpath $(BIN) tests.TestComSimpleClient 127.0.0.2 8888" &

test_S5com:
	xterm -ge 80x15+0+000 -e "java -classpath $(BIN) tests.TestS5Serveur 8888" &
	xterm -ge 80x15+0+230 -e "sleep 1 && java -classpath $(BIN) tests.TestS5Client 127.0.0.2 8888" &
	xterm -ge 80x15+0+460 -e "sleep 1 && java -classpath $(BIN) tests.TestS5Client 127.0.0.2 8888" &

kill_tests:
	pgrep "xterm" | while read pid; do kill -9 $$pid; done

# Création des dossiers
$(BIN):
	mkdir -p $(BIN)

$(JAVADOC):
	mkdir -p $(JAVADOC)


# Supprime les jars
clean:
	rm -f $(JAR_CLIENT) $(JAR_ADMIN) $(JAR_SERVEUR)

# Supprime la doc et tout ce qui peut être supprimé
maxclean: clean
	rm -fr $(JAVADOC)
	rm -fr $(BIN)
	make -C db clean


.PHONY: jars java doc install_db test_communication test_S5com kill_tests clean maxclean


