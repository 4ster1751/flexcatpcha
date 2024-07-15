# flexcaptcha

A minimalistic CAPTCHA generator and validator, with customizable rendering options ready for both
web and desktop
applications. The image manipulation is done
through [https://github.com/ajmas/JH-Labs-Java-Image-Filters](https://github.com/ajmas/JH-Labs-Java-Image-Filters).

## Usage

```java
        AbstractCaptchaRenderer renderer=CaptchaRenderer.getDefaultCaptchaRenderer();

        AbstractCaptchaCipher captchaCipher = CaptchaCipher.builder()
                                                           .expirationTimeSettings(
                                                                new ExpirationTimeSettings( 560L, ()->System.currentTimeMillis()))
                                                           .build();

        AbstractCaptchaGenerator generator = new CaptchaGenerator(captchaCipher,renderer);

        String solution="abc123";
        String salt="someSerializable";

        Captcha captcha=generator.generate(solution,salt);
```
A Captcha generated this way is very basic and not obscured:
![captcha_default](https://github.com/user-attachments/assets/4f900f55-7a40-477a-8b7d-ec73f10ac343)

With the predefined builders, captcha image generation can be further modified to obscure the image, set colors, font, image file format, background and captcha character colors:
```java
AbstractCaptchaRenderer renderer = CaptchaRenderer.builder()
                .availableTextColors(List.of(Color.BLUE, Color.ORANGE, Color.BLACK))
                .noiseSettings(new NoiseSettings(1, Color.GRAY))
                .build();
```
Outputs:
![captcha_basic](https://github.com/user-attachments/assets/0a843e0d-1bb6-40e6-86ef-28f39c2930ce)

Alternatively, an arbitrary chain of effects can be used to further manipulate the rendered image:

```java
TwirlFilter filter = new TwirlFilter();
        float twirlStrength = -0.6f;
        filter.setAngle(twirlStrength);

        AbstractCaptchaRenderer renderer = CaptchaRenderer.builder()
                .noiseSettings(new NoiseSettings(8, Color.BLACK))
                .availableTextColors(List.of(Color.BLUE, Color.RED, Color.ORANGE, Color.MAGENTA, Color.CYAN))
                .maximumLetterRotationAngle(0.45d)
                .imageBackground(new FlatColorBackground(Color.DARK_GRAY))
                .imageOperationsList(Collections.singletonList(filter))
                .build();

        AbstractCaptchaCipher captchaCipher = CaptchaCipher.builder()
                .expirationTimeSettings(new ExpirationTimeSettings(560L, System::currentTimeMillis))
                .build();

        AbstractCaptchaGenerator generator = new CaptchaGenerator(captchaCipher, renderer);
        String solution = "ABCD1234";
        String salt = "someSerializable";
        Captcha captcha = generator.generate(solution, salt);
```
Resulting in:
![captcha_customized](https://github.com/user-attachments/assets/cc66d21f-c310-4afc-97f3-87daf19b940e)

```
<dependency>
    <groupId>io.github.yaforster</groupId>
    <artifactId>flexcaptcha</artifactId>
    <version>2.0.0</version>
</dependency>
```
