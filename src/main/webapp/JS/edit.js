function remove(href) {
    // var href = location.href;
    // console.log(href);
    if (confirm("Are You Sure?")) {
        window.location.href = href;
    }
}