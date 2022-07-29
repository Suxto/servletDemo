function clearKeyword() {
    var url = window.location.href;
    console.log(url);
    var form = document.getElementById("search");
    var keyword = document.getElementById("keyword");
    keyword.value = "";
    form.submit();
}