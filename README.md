# Model Mitosis

`maven 3.8.3`, `Java JDK17`, `Node 14+` and `npm 6+` are required.

## Backend

Build:
```bash
cd backend
mvn clean install
```

Launch:
```bash
cd backend/mandalore-express-infra
mvn spring-boot:run
```
or directly run the `ColumbiadExpressApplication.kt` from IntelliJ

## Frontend

Build:
```bash
cd frontend
npm install
```

Launch:
```bash
cd frontend
npm start
```

then visit http://localhost:3000
