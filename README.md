# flexcaptcha
A minimalistic CAPTCHA generator and validator, with customizable rendering options ready for both web and desktop applications. The image manipulation is done through [https://github.com/ajmas/JH-Labs-Java-Image-Filters](https://github.com/ajmas/JH-Labs-Java-Image-Filters).

## Usage
### text-based CAPTCHA:

```java
    SimpleCaptchaTextGenerator generator = new SimpleCaptchaTextGenerator(); //Can generate randomized strings from a pool of allowed characters
    String s = generator.generate(10, Case.UPPERCASE);
    String pw = "ThisIsMyPassword"; //Supply a password for encryption
    
    SimpleTextImageRenderer renderer = new SimpleTextImageRenderer(); //pick a renderer controlling the image generation (and distortion)
    CipherHandler ch = new CipherHandler(); //Cipherhandler for implementing the encryption and decryption
    
    TextCaptchaHandler handler = new SimpleTextCaptchaHandler();
    String saltSource = "Hello World!"; //A salt source for salting the hashes and encryption
    TextCaptcha captcha = handler.toCaptcha(s, ch, saltSource, pw, renderer , 100, 300); //putting it all together
```

### image-based CAPTCHA:

```java
    ImageCaptchaHandler handler = new SimpleImageCaptchaHandler();
    CipherHandler ch = new CipherHandler();
    ImageLoader loader = new ImageLoader();
    
    BufferedImage[] solutionImages = loader.getImagesfromPath("C:\\SomeDirectory");
    BufferedImage[] fillImages = loader.getImagesfromPath("C:\\SomeOtherDirectory");
    
    String saltSource = "Hello World!";
    int gridWidth = 3;
    ImageCaptcha captcha = handler.generate(gridWidth, ch, saltSource, password, solutionImages, fillImages);
```
## Dependency
Coming soon
