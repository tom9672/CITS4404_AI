const cool = require('cool-ascii-faces');
const express = require('express');
const path = require('path');
const { json } = require('express');

const app = express();

app.use('/', express.static(path.join(__dirname, 'public')));



app.listen(7000, function () {
    console.log('started');
    
});