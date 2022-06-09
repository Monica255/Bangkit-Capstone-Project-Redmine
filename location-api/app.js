const express = require('express');
const bodyParser = require('body-parser');
const koneksi = require('./config/database');
const app = express();
const PORT = process.env.PORT || 5000;

// set body parser
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

// read data / get data
app.get('/getprovinsi', (req, res) => {
    // buat query sql
    const querySql = "SELECT * FROM provinces";

    // jalankan query
    koneksi.query(querySql, (err, rows, field) => {
        // error handling
        if (err) {
            return res.status(500).json({ message: 'Ada kesalahan', error: err });
        }

        // jika request berhasil
        res.status(200).json({ success: true, data: rows });
    });
});

app.get('/getkota/:id', (req, res) => {
    // buat query sql
    const querySql =  "SELECT cities.city_name, province.prov_name AS nama_kota FROM cities INNER JOIN provinces ON cities.prov_id = provinces.prov_id WHERE provinces.prov_id=" + req.params.id;

    // jalankan query
    koneksi.query(querySql, (err, rows, field) => {
        // error handling
        if (err) {
            return res.status(500).json({ message: 'Ada kesalahan', error: err });
        }

        // jika request berhasil
        res.status(200).json({ success: true, data: rows });
    });
});

const api =  functions.https.onRequest(app)

// buat server nya
app.listen(PORT, () => console.log(`Server running at port: ${PORT}`));

module.exports = {
    api
}