# Temperature Converter — Database Localization

JavaFX application that converts temperatures between Celsius, Fahrenheit and
Kelvin. The user interface is localized with FXML and `.properties` files, and
the unit names and descriptions are read from the database in the selected
language.

Author: Ali Al-Khrsan
Course: AD / Sprint 5, Fall 2026

- GitHub: https://github.com/alialkhrsan5/SEP2_inclass2_fx_localization_Ali
- Docker Hub: https://hub.docker.com/r/alii123x/temp-fx-localization

---

## Chosen DB localization approach: translation table

Unit names and descriptions are stored in a separate translation table, linked
to a language-neutral base table by a foreign key.

### Schema

```
unit                          unit_translation
----                          ----------------
id    PK  <-----------------  unit_id   PK, FK
code                          lang      PK
                              name
                              description

conversion
----------
id            PK
input_value
from_unit_id  FK -> unit(id)
to_unit_id    FK -> unit(id)
result_value
lang
created_at
```

`unit` holds only `code` (`celsius`, `fahrenheit`, `kelvin`) — nothing that needs
translating. `unit_translation` holds one row per unit and language, five
languages per unit. `conversion` stores every conversion the user makes: the
value, both units, the result, the language that was active, and a timestamp.

The full schema and the seed data are in `init.sql`. All tables and the
connection use `utf8mb4`, so Japanese and Arabic are stored correctly.

### Why this approach

**Adding a language does not change the schema.** A new language is five new
rows in `unit_translation`. The field approach would need a new column per
language, and the table approach a whole new table plus a code change.

**No data is duplicated.** The unit code exists once. In the row approach the
same non-text data is repeated in every language row, so the rows can drift
apart.

**The fallback is a single query.** `UnitDAO` joins `unit_translation` twice —
once on the requested language, once on English — and uses `COALESCE` to pick
the first one that exists. A missing translation therefore falls back to English
without any extra code or a second round trip. The other three approaches would
need either NULL checks per column or a second query.

The cost is one extra JOIN per query. For three units and five languages that is
irrelevant, and it is the normal trade-off in production systems.

---

## How the two localization layers work together

| Layer | Source | Example |
|---|---|---|
| Static UI labels | `MessagesBundle_xx.properties` via `ResourceBundle` | button texts, column headings, window title |
| Unit names and descriptions | `unit_translation` table via `UnitDAO` | "Celsius", "摂氏", "مئوية" |

Changing the language selector reloads the FXML with a new `ResourceBundle`
**and** re-reads the units from the database with the new language code, so both
layers change at once. When Arabic is selected the scene is set to
right-to-left.

---

## Technologies

Java 21, JavaFX 21.0.6 (controls + FXML), Maven, MariaDB 11.8 with
mariadb-java-client, JUnit 5, JaCoCo, Docker, Jenkins, Kubernetes (minikube),
Xming as the X server.

---

## Tests

| Test class | What it covers |
|---|---|
| `TempCalculatorTest` | every unit pair, boundary values, validation, unknown units |
| `UnitDAOTest` | names in English, Japanese and Arabic, and the English fallback for an unsupported language |
| `ConversionDAOTest` | saving a conversion and reading it back as the newest row |
| `UnitTest`, `ConversionTest` | model objects |

The DAO tests need a running database. If none is reachable they are skipped
rather than failed, so the build also works on a machine without MariaDB.

---

## How to run

### Database

Run `init.sql` in HeidiSQL. It creates the tables and inserts the three units
with translations in all five languages.

```sql
CREATE USER IF NOT EXISTS 'tempuser'@'%' IDENTIFIED BY 'temp123';
GRANT ALL PRIVILEGES ON svg_temp_db.* TO 'tempuser'@'%';
FLUSH PRIVILEGES;
```

### Build and run locally

```bash
mvn clean package
mvn javafx:run
```

### Docker

Start Xming (XLaunch: Multiple windows, Start no client, No Access Control).

```bash
docker build -t alii123x/temp-fx-localization .
docker run --rm -e DISPLAY=host.docker.internal:0.0 alii123x/temp-fx-localization
```

### Kubernetes

```bash
minikube start --driver=docker
docker save alii123x/temp-fx-localization:latest -o app-image.tar
minikube image load app-image.tar
kubectl apply -f k8s-manifests.yaml
kubectl get pods
```

### Jenkins

Pipeline job, *Pipeline script from SCM*, this repository, branch `master`,
script path `Jenkinsfile`. Needs a Maven installation named `MAVEN_HOME` and a
username/password credential with the ID `dockerhub`.
