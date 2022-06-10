# Bangkit-Capstone-Project-Redmine

# Cloud Computing

Cloud Computing part in this project is to set up, manage, deploy and buiild a private API for our application.

## Location API

Our android app needed to display both provinces and cities for user data. We decided to make an API that fetch both provinces data and cities based on provinces id from a MySQL database. Both API and the database will be deployed on Google Cloud Platform with the API using Cloud Run and the MySQL database using Cloud SQL.

### Deploying Database

To deploy the database we first create a Cloud SQL intance and choosing MySQL. After the intance is made, we need to import our database. To do that, we first create a Cloud Storage bucket to store our SQL dump. Then from Cloud SQL we import the SQL dump from the bucket to the instance. After the database is imported we need to set the Instance IP assignment into public in order for the database to be used publicly. After that we need to set the public IP which is 0.0.0.0/0. And that's it! our database is deployed.
