# GOAL
Bangun app Android "UangKu" (manajemen uang pribadi) dengan Kotlin + Jetpack Compose,
build APK di GitHub Actions, repo github.com/Void200sh/uangku.

## Pipeline
- [x] 1. Kredensial GitHub tersimpan (Void200sh, ~/.git-credentials)
- [ ] 2. Preview 3 arah desain dibuka di Chrome → user pilih
- [ ] 3. Scaffold project (gradle 8.7, AGP 8.5.2, Kotlin 1.9.24, Compose BOM 2024.06.00)
- [ ] 4. Data layer: model Transaction, kategori, repository JSON (SharedPreferences)
- [ ] 5. Theme (warna pilihan user) + komponen reusable + dark mode
- [ ] 6. Layar Beranda (saldo, pemasukan/pengeluaran, list transaksi, empty state)
- [ ] 7. Bottom sheet Tambah Transaksi (nominal, jenis, kategori, catatan, tanggal)
- [ ] 8. Layar Statistik (grafik 6 bulan, breakdown kategori)
- [ ] 9. Layar Anggaran (budget per kategori + progress)
- [ ] 10. Icon adaptive + splash + QA checklist UI
- [ ] 11. Push ke GitHub, pantau Actions sampai APK sukses
- [ ] 12. Kirim link APK ke user

## Keputusan
- Kotlin + Compose (bukan XML) — sesuai permintaan user "stack kotlin".
- Storage lokal JSON (org.json bawaan Android) — tanpa Room/KSP = build stabil.
- Package: com.zyvo.uangku, minSdk 26, target/compile 34.
- Gradle dipin 8.7 di workflow supaya tidak ikut versi runner.

## NOW
Step 2 — preview desain sedang dibuka di Chrome, menunggu pilihan user.
