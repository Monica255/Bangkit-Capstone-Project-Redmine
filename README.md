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

### Deploying ML Scripts

To deploy the ML Script we need to create a Virtual Machine instance. To create the VM, we can go to the Compute Engine tab then choose VM Instances and from there just click Create Instance. For the settings, we chose asia-southeast2-a as the zone because it was closer, We then chose n1-standard-1 as our machine because we feel that it was powerful enough and yet not too expensive. We checked both Allow HTTP traffic and Allow HTTPS traffic to allow traffic for both protocol and also put http-server and https-server as the network tags for the firewall rule which will be created later. With all the settings done we created the VM. We need to create a firewall rule to allow http and https. We will create two firewall rules, default-allow-http and default-allow-https. The settings for default-allow-http are put http-server on the network tags, the put the public IP 0.0.0.0/0 on the Source IPv4 ranges, then on Protocols and ports choose Specified protocols and ports and tick TCP then put 80 on TCP. The settings for default-allow-https are similar to default-allow-https which is  put http-server on the network tags, the put the public IP 0.0.0.0/0 on the Source IPv4 ranges, then on Protocols and ports choose Specified protocols and ports and tick TCP then put 443 on TCP. Now we can deploy the ML script

To deploy the script we need to connect to the VM via SSH. In the VM, We need to set up NGINX on the Compute Engine VM. to do that we can run these commands in the terminal
```
sudo apt update
sudo apt install nginx
```
After it was succesfully installed we can checked that NGINX is running buy entering the external IP of our VM into the browser.Next we need to clone the ML scripts from github into the VM. Then we need to install the necessary packages and download the models with these commands in the terminal.
```
sudo apt install python3-pip python3-dev build-essential \
                    libssl-dev libffi-dev python3-setuptools

sudo python3 -m pip install -U pip wheel
sudo python3 -m pip install -U pip python-dotenv
sudo python3 -m pip install gdown
sudo python3 -m pip install -r requirements.txt --ignore-installed httplib2

sudo apt-get install ffmpeg libsm6 libxext6  -y
```
We then need to add the proxy (forwarding) rule for Flask in NGINX settings. More specifically, edit “/etc/nginx/sites-available/default”, with ```sudo nano /etc/nginx/sites-available/default``` Then add the following lines.
```         location / {
                 # First attempt to serve request as file, then
                 # as directory, then fall back to displaying a 404.
                 try_files $uri $uri/ =404;
         }
          #add these line below
 +       location ^~ /api {
 +               proxy_pass http://127.0.0.1:5000;
 +               proxy_set_header Host $host;  # preserve HTTP header for proxy requests
 +       }
```

