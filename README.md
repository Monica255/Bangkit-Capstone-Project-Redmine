# Bangkit-Capstone-Project-Redmine

# Cloud Computing

Cloud Computing part in this project is to set up, manage, deploy and buiild a private API for our application.

## Location API

Our android app needed to display both provinces and cities for user data. We decided to make an API that fetch both provinces data and cities based on provinces id from a MySQL database. Both API and the database will be deployed on Google Cloud Platform with the API using Cloud Run and the MySQL database using Cloud SQL.

### Building The API

To start, we need to initialize a firebase project with npm using the command ```firebase init```. After the project was setup, we need to install needed packages such as express, mysql, and body-parser. To install the packages we can use the command ```npm install express mysql body-parser```. After the packages is installed, all code should be working fine. There are currently 2 routes in the API that do a select MySQL query. The first route is /getprovinsi which will run the query ```"SELECT * FROM provinces"```, the query will return all of the provinces in the provinces table. The second route is /getkota/:id which will run the query ````"SELECT cities.city_name, province.prov_name AS nama_kota FROM cities INNER JOIN provinces ON cities.prov_id = provinces.prov_id WHERE provinces.prov_id=" + req.params.id;````, this query will select the cities from the cities table with the given provinces id. Both is returned in a json format and will be handled in the android project.

### Deploying Database

To deploy the database we first create a Cloud SQL intance and choosing MySQL. After the intance is made, we need to import our database. To do that, we first create a Cloud Storage bucket to store our SQL dump. Then from Cloud SQL we import the SQL dump from the bucket to the instance. After the database is imported we need to set the Instance IP assignment into public in order for the database to be used publicly. After that we need to set the public IP which is 0.0.0.0/0. And that's it! our database is deployed.

### Deploying API to Firebase Cloud Function

To deploy the API to Fribase Cloud Function we hneed to run the command from the API directory terminal. In the terminal we can run ```firebase deploy```. After it was succesfully deployed we can get the url endpoint from firebase in this case it was https://us-central1-redmine-350506.cloudfunctions.net/expressAPI

