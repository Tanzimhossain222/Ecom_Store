$(function () {
    // Initialize all validations
    initUserRegisterValidation();
    initOrdersValidation();
    initResetPasswordValidation();
    initAddProductValidation();
    initAddCategoryValidation();
    addCustomValidationMethods();
});

function initUserRegisterValidation() {
    var $userRegister = $("#userRegister");

    $userRegister.validate({
        rules: {
            name: {
                required: true,
                lettersonly: true
            },
            email: {
                required: true,
                space: true,
                email: true
            },
            mobileNumber: {
                required: true,
                space: true,
                numericOnly: true,
                minlength: 10,
                maxlength: 12
            },
            password: {
                required: true,
                space: true
            },
            confirmpassword: {
                required: true,
                space: true,
                equalTo: '#pass'
            },
            address: {
                required: true,
                all: true
            },
            city: {
                required: true,
                space: true
            },
            state: {
                required: true,
            },
            pincode: {
                required: true,
                space: true,
                numericOnly: true
            },
            img: {
                required: true,
            }
        },
        messages: {
            name: {
                required: 'name required',
                lettersonly: 'invalid name'
            },
            email: {
                required: 'email name must be required',
                space: 'space not allowed',
                email: 'Invalid email'
            },
            mobileNumber: {
                required: 'mob no must be required',
                space: 'space not allowed',
                numericOnly: 'invalid mob no',
                minlength: 'min 10 digit',
                maxlength: 'max 12 digit'
            },
            password: {
                required: 'password must be required',
                space: 'space not allowed'
            },
            confirmpassword: {
                required: 'confirm password must be required',
                space: 'space not allowed',
                equalTo: 'password mismatch'
            },
            address: {
                required: 'address must be required',
                all: 'invalid'
            },
            city: {
                required: 'city must be required',
                space: 'space not allowed'
            },
            state: {
                required: 'state must be required',
                space: 'space not allowed'
            },
            pincode: {
                required: 'pincode must be required',
                space: 'space not allowed',
                numericOnly: 'invalid pincode'
            },
            img: {
                required: 'image required',
            }
        }
    });
}
function initOrdersValidation() {
    var $orders = $("#orders");

    $orders.validate({
        rules: {
            firstName: {
                required: true,
                lettersonly: true
            },
            lastName: {
                required: true,
                lettersonly: true
            },
            email: {
                required: true,
                email: true
            },
            mobileNo: {
                required: true,
                numericOnly: true,
                minlength: 10,
                maxlength: 12
            },
            address: {
                required: true
            },
            city: {
                required: true
            },
            state: {
                required: true
            },
            pincode: {
                required: true,
                numericOnly: true
            },
            paymentType: {
                required: true,
                notEqualTo: "--select--"
            }
        },
        messages: {
            firstName: {
                required: "First name is required.",
                lettersonly: "Only letters are allowed."
            },
            lastName: {
                required: "Last name is required.",
                lettersonly: "Only letters are allowed."
            },
            email: {
                required: "Email is required.",
                email: "Enter a valid email address."
            },
            mobileNo: {
                required: "Mobile number is required.",
                numericOnly: "Enter a valid number.",
                minlength: "Minimum 10 digits required.",
                maxlength: "Maximum 12 digits allowed."
            },
            address: {
                required: "Address is required."
            },
            city: {
                required: "City is required."
            },
            state: {
                required: "State is required."
            },
            pincode: {
                required: "Pincode is required.",
                numericOnly: "Enter a valid pincode."
            },
            paymentType: {
                required: "Please select a payment type.",
                notEqualTo: "Please select a valid option."
            }
        }
    });
}
function initResetPasswordValidation() {
    var $resetPassword = $("#resetPassword");

    $resetPassword.validate({
        rules: {
            password: {
                required: true,
                space: true
            },
            confirmPassword: {
                required: true,
                space: true,
                equalTo: '#pass'
            }
        },
        messages: {
            password: {
                required: 'password must be required',
                space: 'space not allowed'
            },
            confirmPassword: {
                required: 'confirm password must be required',
                space: 'space not allowed',
                equalTo: 'password mismatch'
            }
        }
    });
}
function initAddProductValidation() {
    $("#addProductForm").validate({
        rules: {
            title: {
                required: true,
                minlength: 3
            },
            description: {
                required: true,
                minlength: 10
            },
            category: {
                required: true,
                notEqualTo: "--select--"
            },
            price: {
                required: true,
                number: true,
                min: 0
            },
            isActive: {
                required: true
            },
            stock: {
                required: true,
                number: true,
                min: 0
            },
            file: {
                required: true,
                extension: "jpg|jpeg|png|gif"
            }
        },
        messages: {
            title: {
                required: "Title is required.",
                minlength: "Title must be at least 3 characters long."
            },
            description: {
                required: "Description is required.",
                minlength: "Description must be at least 10 characters long."
            },
            category: {
                required: "Category is required.",
                notEqualTo: "Please select a valid category."
            },
            price: {
                required: "Price is required.",
                number: "Please enter a valid number.",
                min: "Price cannot be negative."
            },
            isActive: {
                required: "Status is required."
            },
            stock: {
                required: "Stock is required.",
                number: "Please enter a valid number.",
                min: "Stock cannot be negative."
            },
            file: {
                required: "Image is required.",
                extension: "Please upload a valid image file (jpg, jpeg, png, gif)."
            }
        }
    });
}

function initAddCategoryValidation() {
    $("#addCategoryForm").validate({
        rules: {
            name: {
                required: true,
                minlength: 3
            },
            isActive: {
                required: true
            },
            file: {
                required: true,
                extension: "jpg|jpeg|png|gif"
            }
        },
        messages: {
            name: {
                required: "Category name is required.",
                minlength: "Category name must be at least 3 characters long."
            },
            isActive: {
                required: "Status is required."
            },
            file: {
                required: "Image is required.",
                extension: "Please upload a valid image file (jpg, jpeg, png, gif)."
            }
        }
    });
}
function addCustomValidationMethods() {
    jQuery.validator.addMethod('lettersonly', function (value, element) {
        return /^[^-\s][a-zA-Z_\s-]+$/.test(value);
    });

    jQuery.validator.addMethod('space', function (value, element) {
        return /^[^-\s]+$/.test(value);
    });

    jQuery.validator.addMethod('all', function (value, element) {
        return /^[^-\s][a-zA-Z0-9_,.\s-]+$/.test(value);
    });

    jQuery.validator.addMethod('numericOnly', function (value, element) {
        return /^[0-9]+$/.test(value);
    });

    jQuery.validator.addMethod("notEqualTo", function (value, element, param) {
        return this.optional(element) || value !== param;
    }, "Invalid selection.");
}
