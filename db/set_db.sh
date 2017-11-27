

# usage : $0 [--install|--uninstall|--insert|--list_tables] host user

# Constante
NOM_BASE="projetS5"
DIR=`dirname $0`

# Fichiers
BASE="$DIR/creation_base.sql"
TABLES="$DIR/creation_tables.sql"
SUPP="$DIR/suppression_base.sql"
INSERT="$DIR/insertion_donnees.sql"

FICHIERS="$BASE $TABLES $SUPP $INSERT"

TMP="$DIR/tmp_db"




# Fonction de gestion des erreurs.
function erreur() {
	case $1 in
		usage)
			cat >&2 << EOD
$0: usage: $0 [--install|--uninstall|--insert|--list_tables] host user
      host, user sont les informations de connexion sql
      --install (par défaut) crée la base et les tables
      --uninstall supprime la base
      --insert ajoute des fake news dans les tables
      --list_tables liste les tables et leurs champs
EOD
			test $# -ge 2 && exit $2
			;;
		fichier)
			echo "$0: le fichier $2 est inaccessible ou manquant" >&2
			test $# -ge 3 && exit $3
			;;
		*)
			echo "$0: erreur inconnue ($1)" >&2
			test $# -ge 2 && exit $2
			;;
	esac
}


# Ajoute des espaces a une chaine pour qu'elle fasse la longueur voulue

function echo_str_spaces(){	#$1=chaine $2=longueur
	lg=`echo "$1" | wc -c`
	str="$1"
	while test $lg -lt $2
	do
		str="$str "
		lg="`expr $lg + 1`"
	done
	echo -n "$str"
}


# Fonction qui formatte un "tableau" avec \t comme séparateur
function mk_tab() {
	while read ligne
	do
		echo_str_spaces "`echo "$ligne" | cut -d "	" -f 1`" 20
		echo_str_spaces "`echo "$ligne" | cut -d "	" -f 2`" 20
		echo_str_spaces "`echo "$ligne" | cut -d "	" -f 3`" 10
		echo_str_spaces "`echo "$ligne" | cut -d "	" -f 4`" 10
		echo ""
	done <&0
}







# Lecture et vérification de la commande passée
test $# -ge 1 || erreur usage 1
action="install"
test "$1" = "--install" -o "$1" = "--uninstall" -o "$1" = "--insert" -o "$1" = "--list_tables" && action="$1" && shift
test $# -eq 2 || erreur usage 1
mysql_user=$2
mysql_host=$1

# Vérification des fichiers
for f in $FICHIERS
do
	test -f "$f" -a -r "$f" || erreur fichier "$f" 2
done

# Obtention du mot de passe mysql
echo -n "Mot de passe mysql: "
trap "stty echo" EXIT HUP INT QUIT
stty -echo
read mysql_passw
stty echo
trap - EXIT HUP INT QUIT
echo ""


# Commande mysql standard
CMD_SQL="mysql --host=$mysql_host --user=$mysql_user --password=$mysql_passw"


case "$action" in

	# Création de la base de donnée et des tables
	--install)
		echo "Création de la base $NOM_BASE"
		$CMD_SQL < "$BASE"
		code=$?
		test $code -ne 0 && exit $code;

		echo "Création des tables"
		$CMD_SQL $NOM_BASE < "$TABLES"
		exit $?
		;;
		
	# Suppression de la base
	--uninstall)
		echo "Suppression de la base $NOM_BASE"
		$CMD_SQL $NOM_BASE < "$SUPP"
		exit $?
		;;
	
	# Insertion des données
	--insert)
		echo "Insertion des données"
		$CMD_SQL $NOM_BASE < "$INSERT"
		exit $?
		;;
	
	# Liste des tables existantes
	--list_tables)
		echo "SHOW TABLES" | $CMD_SQL $NOM_BASE | grep -v "^Tables_in" > $TMP
		echo "`wc -l "$TMP" | cut -d " " -f 1` tables dans la base $NOM_BASE : "
		cat "$TMP" | while read table
		do
			echo ""
			echo "Table '$table' :"
			echo "DESCRIBE $table" | $CMD_SQL $NOM_BASE | mk_tab
		done
		rm -f "$TMP"*
		exit 0
		;;
	
	*)
		erreur inconnue 10
		;;

	
esac


