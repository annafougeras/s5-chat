# Dossiers
SRC = src
BIN = bin
DOC = doc
JAVADOC = $(DOC)/javadoc

# Packages nécessaires
PACKAGES_CLIENT = app vue controleur modele communication communication.simple commChatS5
PACKAGES_ADMIN = app vue controleur modele communication communication.simple commChatS5
PACKAGES_SERVEUR = serveur modele communication communication.simple commChatS5

PACKAGES_TESTS = tests communication communication.simple commChatS5 modele

# Liste des packages utiles (sort trie et enlève les doublons)
PACKAGES_TOUT = $(sort $(PACKAGES_CLIENT) $(PACKAGES_ADMIN) $(PACKAGES_SERVEUR) $(PACKAGES_TESTS))

# Jars (exécutables)
JAR_CLIENT  = appli_client.jar
JAR_ADMIN   = appli_admin.jar
JAR_SERVEUR = serveur.jar
JARS = $(JAR_CLIENT) $(JAR_ADMIN) $(JAR_SERVEUR)

# Fichiers manifest des jars
MANIFEST_CLIENT = $(SRC)/manifest_appli_client.txt
MANIFEST_ADMIN  = $(SRC)/manifest_appli_admin.txt
MANIFEST_SERVEUR= $(SRC)/manifest_serveur.txt

# Fichiers
F_JAVA = $(foreach package, $(PACKAGES_TOUT), $(filter-out %/package-info.java, $(wildcard $(SRC)/$(subst .,/,$(package))/*.java)))
F_CLASS = $(foreach f, $(F_JAVA), $(subst $(SRC)/, $(BIN)/, $(f:.java=.class)))



# Tout
all: java $(JARS)


# Compile le java (.java -> .class)
java: $(F_CLASS)

$(F_CLASS): | $(BIN)
	javac -sourcepath $(SRC) -d $(BIN) $(foreach package, $(PACKAGES_TOUT), $(SRC)/$(subst .,/,$(package))/*.java)


# Installe la base de donnée (cf. makefile du dossier db/)
install_db: 
	make -C db install


# Crée les jars
$(JAR_CLIENT): $(F_CLASS) $(MANIFEST_CLIENT)
	jar cmf $(MANIFEST_CLIENT) $@ $(foreach package, $(PACKAGES_CLIENT), -C $(BIN) $(subst .,/,$(package)))
	
$(JAR_ADMIN): $(F_CLASS) $(MANIFEST_ADMIN)
	jar cmf $(MANIFEST_ADMIN) $@ $(foreach package, $(PACKAGES_ADMIN), -C $(BIN) $(subst .,/,$(package)))
	
$(JAR_SERVEUR): $(F_CLASS) $(MANIFEST_SERVEUR)
	jar cmf $(MANIFEST_SERVEUR) $@ $(foreach package, $(PACKAGES_SERVEUR), -C $(BIN) $(subst .,/,$(package)))





# Javadoc
doc: | $(JAVADOC)
	javadoc -use -quiet -public $(PACKAGES_TOUT) -sourcepath $(SRC) -d $(JAVADOC) -charset "UTF-8"

doc_private: | $(JAVADOC)
	javadoc -use -quiet -private $(PACKAGES_TOUT) -sourcepath $(SRC) -d $(JAVADOC) -charset "UTF-8"


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


.PHONY: jars java doc doc_private install_db test_communication test_S5com kill_tests clean maxclean


