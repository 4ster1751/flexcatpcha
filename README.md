# flexcaptcha

A minimalistic CAPTCHA generator and validator, with customizable rendering options ready for both
web and desktop
applications. The image manipulation is done
through [https://github.com/ajmas/JH-Labs-Java-Image-Filters](https://github.com/ajmas/JH-Labs-Java-Image-Filters).

## Usage

```java
AbstractCaptchaRenderer renderer=CaptchaRenderer.getDefaultCaptchaRenderer();
        AbstractCaptchaCipher captchaCipher=CaptchaCipher.builder()
        .expirationTimeSettings(new ExpirationTimeSettings(560L,()->System.currentTimeMillis()))
        .build();

        AbstractCaptchaGenerator generator=new CaptchaGenerator(captchaCipher,renderer);
        String solution="abc123";
        String salt="someSerializable";
        Captcha captcha=generator.generate(solution,salt);
```

Alternatively, an arbitrary chain of effects can be used to further manipulate the rendered image:

```java
TwirlFilter filter=new TwirlFilter();
        float twirlStrength=-0.3f;
        filter.setAngle(twirlStrength);

        AbstractCaptchaRenderer renderer=CaptchaRenderer.builder()
        .imageOperationsList(Collections.singletonList(filter))
        .build();

        AbstractCaptchaCipher captchaCipher=CaptchaCipher.builder()
        .expirationTimeSettings(new ExpirationTimeSettings(560L,()->System.currentTimeMillis()))
        .build();

        AbstractCaptchaGenerator generator=new CaptchaGenerator(captchaCipher,renderer);
        String solution=generateCaptchaSolution();
        Captcha captcha=generator.generate(solution,salt);
```

```
<dependency>
    <groupId>io.github.yaforster</groupId>
    <artifactId>flexcaptcha</artifactId>
    <version>2.0.0</version>
</dependency>
```
