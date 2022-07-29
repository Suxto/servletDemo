function remove() {
    // console.log(typeof location.href);
    var href = location.href;
    if (confirm("Are You Sure?")) {
        window.location.href = href+"&remove=true";
    }
}