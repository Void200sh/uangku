# GOAL
Bangun app Android "UangKu" (manajemen uang pribadi) dengan Kotlin + Jetpack Compose,
build APK di GitHub Actions, repo github.com/Void200sh/uangku.

## Pipeline
- [x] 1. Kredensial GitHub tersimpan (Void200sh, ~/.git-credentials)
- [ ] 2. Preview 3 arah desain dibuka di Chrome → user pilih
- [x] 3. Scaffold project (gradle 8.7, AGP 8.5.2, Kotlin 1.9.24, Compose BOM 2024.06.00)
- [x] 4. Data layer: model Transaction, kategori, repository JSON (SharedPreferences)
- [x] 5. Theme (warna pilihan user) + komponen reusable + dark mode
- [x] 6. Layar Beranda (saldo, pemasukan/pengeluaran, list transaksi, empty state)
- [x] 7. Bottom sheet Tambah Transaksi (nominal, jenis, kategori, catatan, tanggal)
- [x] 8. Layar Statistik (grafik 6 bulan + breakdown) (grafik 6 bulan, breakdown kategori)
- [x] 9. Layar Anggaran (budget per kategori + progress)
- [x] 10. Icon adaptive + dark mode toggle + splash + QA checklist UI
- [ ] 11. Push ke GitHub — TERHALANG: token belum punya scope `workflow`, pantau Actions sampai APK sukses
- [ ] 12. Kirim link APK ke user

## Keputusan
- Kotlin + Compose (bukan XML) — sesuai permintaan user "stack kotlin".
- Storage lokal JSON (org.json bawaan Android) — tanpa Room/KSP = build stabil.
- User konfirmasi 2026-10-29: **tanpa backend**, data lokal saja.
- Package: com.zyvo.uangku, minSdk 26, target/compile 34.
- Gradle dipin 8.7 di workflow supaya tidak ikut versi runner.

## NOW
Kode sumber SUDAH di-push ke github.com/Void200sh/uangku (tanpa workflow).
Commit workflow masih lokal. Menunggu token dengan scope `workflow` → push commit CI → build APK.
