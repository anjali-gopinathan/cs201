/**
 * 
 */

var navbarhtml = '<div class="main-nav">\
			<div class="logo">\
				<a href="index.html" id="logo-link">SalStocks</a>\
			</div>\
			<div class="navbar">\
				<div id="login"><a href="login.html">Log in/Sign up</a></div>\
				<div id="logout" onclick="signOut()"><a href="index.html">Logout</a></div>\
                <div id="home"><a href="index.html">Home/Search</a></div>\
				<div id="portfolio"><a href="portfolio.html">Portfolio</a></div>\
				<div id="favorites"><a href="favorites.html">Favorites</a></div>\
			</div>\
            <div class="alert-banner"></div>\
		</div>';
//document.getElementById('nav').innerHTML = navbarhtml;
document.write(navbarhtml);


$(window).on("load", function(){
    navBarButtons();
    displayAll();
});


function onSignIn(googleUser) {
    var profile = googleUser.getBasicProfile();
    setUsernameCookie(profile.getEmail());
    navBarButtons();
}
function signOut() {
	console.log("signing out");
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
        setUsernameCookie("");
        navBarButtons();
    });
}
function setUsernameCookie(username) {
    var d = new Date();
    d.setTime(d.getTime() + (1*60*60*1000)); // login lasts for one hour
    var expires = "expires="+ d.toUTCString();
    document.cookie = "username=" + username + ";" + expires + ";path=/";
}
function checkUsernameCookie() {
    var usernameCookie = "username=";
    var cookies = document.cookie;
    var split = cookies.split(';');
    for (var i = 0; i < split.length; i++) {
        var cookie = split[i];
        while(cookie.charAt(0) == ' ') {
            cookie = cookie.substring(1);
        }
        if (cookie.indexOf(usernameCookie) == 0) {
            return cookie.substring(usernameCookie.length, cookie.length);
        }
    }
    return "";
}
// sign in functions
function navBarButtons() {
    if (checkUsernameCookie() == "") {
        $("#login").show();
        $("#logout").hide();
        $("#portfolio").hide();
        $("#favorites").hide();
    } else {
        $("#login").hide();
        $("#logout").show();
        $("#portfolio").show();
        $("#favorites").show();
        $("#oauth-signin").hide();
    }
}
