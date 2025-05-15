const express = require('express');
const router = express.Router();
const User = require('../Models/User');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

// REGISTER
router.post('/register', async (req, res) => {
  const { namaLengkap, email, password, confirmPassword } = req.body;

  if (!namaLengkap || !email || !password || !confirmPassword) {
    return res.status(400).json({ message: 'Harap lengkapi semua data' });
  }

  if (password !== confirmPassword) {
    return res.status(400).json({ message: 'Password dan konfirmasi tidak cocok' });
  }

  try {
    const existingUser = await User.findOne({ email });
    if (existingUser)
      return res.status(400).json({ message: 'Email sudah terdaftar' });

    // Tentukan role berdasarkan email
    let role = 'donatur'; // default
    if (email === 'master@gmail.com' && password === '12345678') {
      role = 'admin';
    } else if (email === 'pengalang@gmail.com' && password === '12345678') {
      role = 'organisasi';
    }

    const hashedPassword = await bcrypt.hash(password, 10);

    const newUser = new User({
      namaLengkap,
      email,
      password: hashedPassword,
      role
    });

    await newUser.save();

    res.status(201).json({ message: 'Registrasi berhasil' });
  } catch (err) {
    res.status(500).json({ message: 'Terjadi kesalahan server' });
  }
});

// LOGIN
router.post('/login', async (req, res) => {
  const { email, password } = req.body;

  if (!email || !password)
    return res.status(400).json({ message: 'Email dan password wajib diisi' });

  try {
    const user = await User.findOne({ email });
    if (!user)
      return res.status(400).json({ message: 'Email tidak ditemukan' });

    const isMatch = await bcrypt.compare(password, user.password);
    if (!isMatch)
      return res.status(400).json({ message: 'Password salah' });

    const token = jwt.sign({ userId: user._id, role: user.role }, process.env.JWT_SECRET, {
      expiresIn: '1d'
    });

    res.json({
      message: 'Login berhasil',
      token,
      user: {
        id: user._id,
        namaLengkap: user.namaLengkap,
        email: user.email,
        role: user.role
      }
    });
  } catch (err) {
    res.status(500).json({ message: 'Terjadi kesalahan server' });
  }
});

module.exports = router;
