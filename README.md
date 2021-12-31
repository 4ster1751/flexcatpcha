#flexcaptcha
A minimalistic CAPTCHA generator and validator, with customizable rendering options ready for both web and desktop applications. The image manipulation is done through [https://github.com/ajmas/JH-Labs-Java-Image-Filters](https://github.com/ajmas/JH-Labs-Java-Image-Filters).
##Usage
###text-based CAPTCHA:
    SimpleCaptchaTextGenerator generator = new SimpleCaptchaTextGenerator();
    String s = generator.generate(10, Case.UPPERCASE);
    String pw = "ThisIsMyPassword";
    
    SimpleTextImageRenderer renderer = new SimpleTextImageRenderer();
    CipherHandler ch = new CipherHandler();
    
    TextCaptchaHandler handler = new SimpleTextCaptchaHandler();
    String saltSource = "Hello World!";
    TextCaptcha captcha = handler.toCaptcha(s, ch, saltSource, pw, renderer , 100, 300);
###image-based CAPTCHA:
    ImageCaptchaHandler handler = new SimpleImageCaptchaHandler();
    CipherHandler ch = new CipherHandler();
    ImageLoader loader = new ImageLoader();
    
    BufferedImage[] solutionImages = loader.getImagesfromPath("C:\\SomeDirectory");
    BufferedImage[] fillImages = loader.getImagesfromPath("C:\\SomeOtherDirectory");
    
    String saltSource = "Hello World!";
    int gridWidth = 3;
    ImageCaptcha captcha = handler.generate(gridWidth, ch, saltSource, password, solutionImages, fillImages);
##Dependency
Coming soon
