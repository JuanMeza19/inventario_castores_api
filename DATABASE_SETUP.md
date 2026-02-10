# Database Setup Instructions

## MySQL Database Configuration

The application is configured to connect to a MySQL database named `castores_db`.

### Current Configuration:
- **Database**: castores_db
- **Host**: localhost:3306
- **User**: springuser
- **Password**: pring@123!

### Database Setup Steps:

1. **Login to MySQL:**
   ```bash
   sudo mysql -u root -p
   ```

2. **Create the database:**
   ```sql
   CREATE DATABASE IF NOT EXISTS castores_db;
   ```

3. **Create user and grant privileges (if needed):**
   ```sql
   CREATE USER IF NOT EXISTS 'springuser'@'localhost' IDENTIFIED BY 'pring@123!';
   GRANT ALL PRIVILEGES ON castores_db.* TO 'springuser'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Verify database creation:**
   ```sql
   SHOW DATABASES;
   ```

4. **Exit MySQL:**
   ```sql
   EXIT;
   ```

### Alternative: Use a different MySQL user

If you prefer to use a different user, update the `application.properties` file:

```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Testing the Connection

After setting up the database, run:
```bash
mvn spring-boot:run
```

The application should start successfully and connect to the database.