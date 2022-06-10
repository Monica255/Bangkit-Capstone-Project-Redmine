const mysql = require('mysql');
// buat konfigurasi koneksi
const koneksi = mysql.createConnection({
    host: '34.101.186.33',
    user: 'root',
    password: 'redmine123',
    database: 'db_wilayah_indonesia',
    multipleStatements: true
});

module.exports = koneksi;
