# CallSearcher
## Зачем-то работает даже во время исходящего звонка

Ссылка на *.apk: https://yadi.sk/d/WoAO38BatjNTf

На андроиде 6.0 и выше при запуске требует разрешение на рисование поверх других окон и доступ к телефону, после чего закрывается

![](https://github.com/AudasViator/CallSearcher/raw/master/animation.gif)


Во время звонка можно получить intent из BroadcastReceiver`а. Но, начиная с Android версии 3.1, нужно хотя бы раз запустить своё activity, иначе никакие интенты не придут: защита от различных нехороших вещей. Поэтому здесь есть активити, которая запускается сразу после загрузки устройства (на 6.0 и выше не работает т.к. не запрашивает разрешение, чтобы не усложнять код), и сразу же закрывается

Работает при входящем и исходящем звонке на:
- Эмуляторах
- Sony Z1 Compact с 5.1
- Sony Z2 с 6.0
- Samsung S7 Edge с 6.0
