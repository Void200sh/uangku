# GOAL
Bangun app Android "UangKu" (manajemen uang pribadi) dengan Kotlin + Jetpack Compose,
build APK di GitHub Actions, repo github.com/Void200sh/uangku.

## v1 (SELESAI)
- [x] 1. Kredensial GitHub tersimpan (Void200sh, ~/.git-credentials)
- [x] 3. Scaffold project (gradle 8.7, AGP 8.5.2, Kotlin 1.9.24, Compose BOM 2024.06.00)
- [x] 4. Data layer: model Transaction, kategori, repository JSON (SharedPreferences)
- [x] 5. Theme (Emerald) + komponen reusable + dark mode
- [x] 6. Layar Beranda
- [x] 7. Bottom sheet Tambah Transaksi
- [x] 8. Layar Statistik (grafik 6 bulan + breakdown)
- [x] 9. Layar Anggaran (budget per kategori + progress)
- [x] 10. Icon adaptive + dark mode toggle + splash
- [x] 11. Push + build Actions sukses (tag apk-2), APK 14.9 MB
- [x] 12. Link APK dikirim

## v2 — update UI + target barang (SEDANG JALAN)
- [x] 13. Model `Goal` (id, title, target, saved, photo, createdAt) + `balanceSeries()` 30 hari
- [x] 14. `PhotoStore` — foto target disimpan JPEG q85 ≤1000px di filesDir/goals (tahan walau galeri dihapus)
- [x] 15. Store: key `goals`, StateFlow goals, addGoal / addToGoal / deleteGoal, read/write JSON
- [x] 16. `GoalPhoto` composable async + placeholder gradien
- [x] 17. Home: kurva saldo gaya crypto (Canvas cubic bezier + gradient fill), label "30 HARI", diff text
- [x] 18. Layar `GoalsScreen`: kartu foto + progress, dialog tambah (photo picker PickVisualMedia), dialog nabung, hapus
- [x] 19. App.kt: tab ke-3 "Target" (Icons.Filled.Flag), FAB transaksi disembunyikan di tab Target, wiring add/fund/delete + hapus file foto
- [x] 20. Rapikan BalanceCurve (pakai series dari HomeScreen, minOrNull/maxOrNull)
- [ ] 21. Commit + push + workflow Actions sukses
- [ ] 22. Download APK ke folder sesi (install lokal, tanpa Play Store) + kirim instruksi

## v3 — multi tema + import/export (SEDANG JALAN)
- [x] 23. Theme engine: 4 palet (Emerald, Pixel, Senja, Lavender) × terang/gelap, `AppTheme.fromId`
- [x] 24. Store: `themeId` + `setThemeId`, `replaceAll()` untuk import
- [x] 25. `Backup.kt`: export/import JSON (transaksi, anggaran, target + foto base64)
- [x] 26. `SettingsScreen`: pilih tema (kartu gradien), mode Sistem/Terang/Gelap, export/import + dialog konfirmasi, info penyimpanan
- [x] 27. Home: ikon gear buka Pengaturan (ganti toggle tema lama)
- [x] 28. MainActivity: tema dari Store; versi 2.0 (versionCode 2)
- [x] 29. Push + build Actions sukses (apk-4), APK 16 MB diunduh ke Download/UangKu-2.0.apk

## Keputusan
- Kotlin + Compose (bukan XML) — sesuai permintaan user "stack kotlin".
- Storage lokal JSON (org.json bawaan Android) — tanpa Room/KSP = build stabil.
- User konfirmasi 2026-10-29: **tanpa backend**, data lokal saja.
- Foto target disalin ke internal app (bukan URI galeri) supaya permanen.
- Tab order: Beranda, Statistik, Target, Anggaran. FAB + tidak tampil di tab Target.
- "Install lokal" = APK diunduh lalu di-install langsung dari file di HP (bukan Play Store).
  HP tidak dipakai untuk compile gradle — build tetap di Actions (aturan wajib).
- Gradle dipin 8.7 di workflow supaya tidak ikut versi runner.

- Debug APK tiap build punya tanda tangan berbeda (keystore runner acak) → update dari
  versi lama HARUS uninstall dulu; makanya export/import wajib ada. Kalau mau update
  tanpa uninstall, perlu keystore tetap (belum dikerjakan).

## NOW
v3 selesai & APK terkirim (apk-4, versi 2.0). Menunggu feedback user.
