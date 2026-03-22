## CreatureCapture - Contribution

### Local development

Requirements:

- Java 17
- Gradle wrapper included in the repository

Typical commands:

```bash
./gradlew build
./gradlew test
./gradlew printVersion
```

### Documentation

This repository uses MkDocs with the shared Stellionix documentation package.

Relevant files:

- `mkdocs.yml`
- `requirements.txt`
- `docs/`
- `.github/workflows/docs.yml`

### CI

- `.github/workflows/ci.yml` builds the plugin and runs tests.
- `.github/workflows/docs.yml` deploys documentation when doc-related files change on `main`.
