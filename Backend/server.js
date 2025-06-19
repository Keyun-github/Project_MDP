const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
require('dotenv').config();

const authRoutes = require('./routes/auth');

const app = express();

// Middleware
app.use(cors());
app.use(express.json());
app.use('/api/auth', authRoutes);

// Cek koneksi MongoDB
if (!process.env.MONGO_URI) {
  console.error('MONGO_URI tidak ditemukan di .env');
  process.exit(1);
}
if (!process.env.JWT_SECRET) {
  console.error('JWT_SECRET tidak ditemukan di .env');
  process.exit(1);
}

// Koneksi ke MongoDB dan jalankan server
mongoose.connect(process.env.MONGO_URI, {
  useNewUrlParser: true,
  useUnifiedTopology: true
}).then(() => {
  console.log('MongoDB connected');
  app.listen(process.env.PORT, () => {
    console.log(`Server running on port ${process.env.PORT}`);
  });
}).catch(err => {
  console.error('MongoDB connection error:', err.message);
});
