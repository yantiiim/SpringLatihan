$(document).ready(function(){
    $.getJSON("/listprovinsijson", function(data){
        let cekBoxDiv = '';
        $.each(data, function (key, val) {
            cekBoxDiv +="<option value="+val.idProvinsi+">"+val.namaProvinsi+"</option>";
        });
        $("#dataprovinsi").html(cekBoxDiv);
    });
})

//$(document).ready(function(){
//    $.getJSON("/listkabjson", function(data){
//        let cekBoxDiv = '';
//        $.each(data, function (key, val) {
//            cekBoxDiv +="<option value="+val.idKabupaten+">"+val.namaKabupaten+"</option>";
//        });
//        $("#datakab").html(cekBoxDiv);
//    });
//})

function getKab(id){
     $.getJSON("/listkabjson/"+id, function(data){
        let cekBoxDiv = '';
        $.each(data, function (key, val) {
            cekBoxDiv +="<option class='form-control' value="+val.idKabupaten+">"+val.namaKabupaten+"</option>";
        });
        $("#datakab").html(cekBoxDiv);
    });
}
