# Wanderscope API

REST API of the Wanderscope App created by Jan Steuer as the diploma thesis project.

### About 💡
Wanderscope is a mobile app that allows organizing trips with other travelers. Wanderscope will create an itinerary according to your plans, help you with splitting group expenses and save your travel documents.

### Features 👓
*  Login with email (*for now*), change password and forgot password features
*  Authorization using rotating JWT tokens (access and refresh tokens)
*  Invite to trip other travellers and manage each authorities
*  Upload and download files (encrypted and key-protected)
*  Create or join to trips, add points of interests
*  Split group expenses

### Build with 🛠️
* **Ktor** -- Ktor is an asynchronous framework for creating microservices, web applications and more. Written in Kotlin from the ground up.
* **Exposed + PostgreSQL** -- ORM framework for Kotlin. Offers two levels of database access: typesafe SQL wrapping DSL and lightweight data access objects. 
* **HikariCP** -- Fast, simple, reliable. HikariCP is a "zero-overhead" production ready JDBC connection pool.
* **AWS-S3** -- Amazon Simple Storage Service (Amazon S3) is an object storage service offering industry-leading scalability, data availability, security, and performance. 

### Deployment ☁️
Automatically deployed to Heroku using Github Actions. Runs in *staging* and *production* environment using Heroku pipelines.
