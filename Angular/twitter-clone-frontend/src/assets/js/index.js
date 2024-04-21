document.onscroll = function() {
    if ( window.innerHeight + window.scrollY > document.body.clientHeight){
        document.getElementById("view").style.display='none';
    }else {
        document.getElementById("view").style.display='block';
    }
}