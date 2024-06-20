package com.TeamBangkit.animaldetection.View.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.TeamBangkit.animaldetection.Animal
import com.TeamBangkit.animaldetection.R

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Ini Halaman Home"
    }
    val text: LiveData<String> = _text

    private val _animals = MutableLiveData<List<Animal>>().apply {
        value = listOf(
            Animal("Anjing", "10 tahun","Mamalia","Kebun Binatang Surabaya","Anjing adalah hewan pemangsa dan hewan pemakan bangkai, memiliki gigi tajam dan rahang yang kuat untuk menyerang, menggigit, dan mencabik-cabik makanan. Ciri-ciri khas dari moyang serigala masih bertahan pada anjing, walaupun penangkaran secara selektif telah berhasil mengubah bentuk fisik berbagai jenis anjing ras", R.drawable.anjing),
            Animal("Burung Parot", "5 tahun","Burung","Kebun Binatang Surabaya","Burung Parrot memiliki ukuran kepala lebih besar dibandingkan dengan tubuhnya dan terdapat bulu jambul yang khas pada bagian depan kepala. Burung ini memiliki paruh yang sangat besar, mirip dengan paruh makaw hyacinth, sehingga membuatnya dapat memecahkan biji-bijian yang keras. Hasil penelitian terhadap tulang-tulang (subfosil) mengindikasikan bahwa spesies ini memiliki perbedaan ukuran yang besar pada ukuran kepala dan ukuran tubuh secara keseluruhan dengan burung-burung bayan yang masih hidup", R.drawable.burung_parrot),
            Animal("Harimau", "15 tahun","Mamalia","Kebun Binatang Surabaya","Harimau (Panthera tigris) atau maung adalah spesies kucing terbesar yang masih hidup dari genus Panthera. Harimau memiliki ciri loreng yang khas pada bulunya, berupa garis-garis vertikal gelap pada bulu oranye, dengan bulu bagian bawah berwarna putih. Harimau adalah pemangsa puncak, mereka terutama memangsa ungulata seperti rusa dan babi celeng. Harimau adalah hewan teritorial dan umumnya merupakan pemangsa soliter yang penyendiri, tetapi tetap memiliki sisi sosial", R.drawable.harimau),
            Animal("Jerapah", "18 tahun","Mamalia","Kebun Binatang Surabaya","Jerapah adalah mamalia berkuku besar Afrika yang termasuk dalam genus Giraffa. Ini adalah hewan darat tertinggi yang masih hidup dan pemamah biak terbesar di Bumi. Secara tradisional, jerapah dianggap sebagai satu spesies , Giraffa camelopardalis , dengan sembilan subspesies . Baru-baru ini, para peneliti mengusulkan untuk membagi mereka menjadi delapan spesies yang masih ada karena penelitian baru terhadap DNA mitokondria dan inti mereka , serta pengukuran morfologi", R.drawable.jerapah),
            Animal("Love Bird", "3 tahun","Burung","Kebun Binatang Surabaya","Burung cinta (Bahasa Inggris: Lovebird ) adalah satu dari sembilan burung jenis spesies genus Agapornis (dari bahasa Yunani \"agape\" yang berarti \"cinta\" dan \"ornis\" yang berarti \"burung\"). Mereka adalah burung yang berukuran kecil, antara 13 sampai 17 cm dengan berat 40 hingga 60 gram, dan bersifat sosial. Delapan dari spesies ini berasal dari Afrika, sementara spesies burung cinta kepala abu-abu berasal dari Madagaskar", R.drawable.love_bird),
            Animal("Panda", "30 tahun","Mamalia","Kebun Binatang Surabaya"," seekor mamalia yang diklasifikasikan ke dalam keluarga beruang, Ursidae, yang merupakan hewan asli Tiongkok Tengah. Secara taksonomi panda tergolong karnivora", R.drawable.panda),
            // Tambahkan data hewan lainnya di sini
        )
    }
    val animals: LiveData<List<Animal>> = _animals
}