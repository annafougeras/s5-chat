
# Réglages mysql
HOST  = 127.0.0.1
USER  = root



install:
	bash ./set_db.sh --install $(HOST) $(USER)

uninstall:
	bash ./set_db.sh --uninstall $(HOST) $(USER)

fake_data:
	bash ./set_db.sh --insert $(HOST) $(USER)

list_tables:
	bash ./set_db.sh --list_tables $(HOST) $(USER)

clean:
	rm -f log*

.PHONY: install uninstall fake_data list_tables clean
