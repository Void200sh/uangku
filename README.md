# UangKu

App manajemen uang pribadi untuk Android. Kotlin + Jetpack Compose, data lokal (offline), tanpa akun.

## Fitur
- Saldo total + ringkasan pemasukan/pengeluaran bulan ini
- Catat transaksi (keypad nominal, kategori, catatan, tanggal)
- Tahan lama pada transaksi untuk menghapus
- Statistik: grafik 6 bulan + rincian per kategori
- Anggaran per kategori dengan progress
- Dark mode (ikut sistem, bisa di-toggle)

## Stack
- Kotlin 1.9.24, Jetpack Compose (BOM 2024.06.00), Material 3
- AGP 8.5.2 / Gradle 8.7 / JDK 17
- minSdk 26, targetSdk 34
- Storage: JSON di SharedPreferences (`data/Store.kt`)

## Build
Otomatis lewat GitHub Actions: setiap push ke `main` menghasilkan APK debug di
tab Actions / Releases.
