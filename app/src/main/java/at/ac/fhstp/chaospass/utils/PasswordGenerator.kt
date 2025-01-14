package at.ac.fhstp.chaospass.utils


fun generateRandomPassword(length: Int = 12): String {
    val upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val lowerCase = "abcdefghijklmnopqrstuvwxyz"
    val digits = "0123456789"
    val specialChars = "!@#\$%^&*()-_=+<>?{}[]"
    val allChars = upperCase + lowerCase + digits + specialChars

    // Ensure the password includes at least one character from each set
    val randomPassword = StringBuilder()
    randomPassword.append(upperCase.random())
    randomPassword.append(lowerCase.random())
    randomPassword.append(digits.random())
    randomPassword.append(specialChars.random())

    // Fill the rest of the password with random characters
    for (i in 4 until length) {
        randomPassword.append(allChars.random())
    }

    // Shuffle the password to make it unpredictable
    return randomPassword.toList().shuffled().joinToString("")
}
