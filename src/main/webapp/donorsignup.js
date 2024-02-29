/**
 * 
 */

function signinpage() {
	console.log("going to sign in");
	alert("going to signin");
	window.location.href = "donorsignin.html";
}


		var form = document.getElementById("form");
		form.addEventListener('submit', function (event) {
			var donorName = document.getElementById("donorname").value;
			var donorEmail = document.getElementById("donoremail").value;
			var donorPassword = document.getElementById("donorpassword").value;
			
			if(donorName.trim()!='' && donorEmail.trim()!='' && donorPassword.trim()!=''){
				//name, email and password is not empty
			if (donorEmail.includes("@") && donorEmail.includes(".com")) {
				if(donorPassword.length>8){//checking password lenght
					//password lenght is more then 8
					//if everything is correct then go ahead
				alert("submitting form");
				return true;
			
				}
				else{
					//password length is less then 8
					document.getElementById("donorpasswod").addEventListener("invalid", function () {
					// Set a custom error message
					this.setCustomValidity("Password must be at least 8 characters");
				});

				// Attach an event listener to reset the custom validity when the input is valid
				document.getElementById("donorpasswod").addEventListener("input", function () {
					this.setCustomValidity("");
				});

				alert("preventing form from submisstion");
				event.preventDefault();
				}
				}
			else {
				document.getElementById("donoremail").addEventListener("invalid", function () {
					// Set a custom error message
					this.setCustomValidity("Please enter a valid Email");
				});

				// Attach an event listener to reset the custom validity when the input is valid
				document.getElementById("donoremail").addEventListener("input", function () {
					this.setCustomValidity("");
				});
				
				alert("preventing form from submisstion");
				event.preventDefault();
			}
			}
			else{
				alert("preventing form from submisstion");
				event.preventDefault();
			}


		})