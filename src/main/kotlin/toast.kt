package com.example.demo

import javafx.animation.Animation
import javafx.animation.FadeTransition
import javafx.animation.TranslateTransition
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.transform.Translate
import java.nio.file.Paths

enum class ImageStyle {
    CIRCLE,
    RECTANGLE
}
enum class AnimationType {
    TRANSITION,
    TRANSLATE
}
enum class Position {
    RIGHT_BOTTOM,
    RIGHT_TOP,
    LEFT_BOTTOM,
    LEFT_TOP
}
class Config {
    var alpha = 0.9
    var openTime = 7000.0
    var imageType = ImageStyle.RECTANGLE
    var title = "TITLE"
    var message = "MESSAGE"
    var appName = "APP NAME"
    var image = "https://avatars.mds.yandex.net/i?id=ee0a8cd0c69a411b7fee131fde2b4980-3732926-images-thumbs&n=13"
    var soundEffect = "C:/Users/pen/IdeaProjects/laba/demo/src/main/resources/new_message_tone.wav"
    var animation = AnimationType.TRANSLATE
    var position = Position.RIGHT_BOTTOM
}

class Toast {
    private var config = Config()
    private val windows = Stage()
    private var root = BorderPane()
    private var box = HBox()
    class Builder {
        private var config = Config()

        fun setTitle(str: String): Builder {
            config.title = str
            return this
        }

        fun setMessage(str: String): Builder {
            config.message = str;
            return this
        }

        fun setAppName(str: String): Builder {
            config.appName = str
            return this
        }

        fun build(): Toast  {
            var toast = Toast()
            toast.config = config
            toast.build()
            return toast
        }
    }

    private fun playNotify() {
        val media = Media(Paths.get(config.soundEffect).toUri().toString())
        val mediaPlayer = MediaPlayer(media)
        mediaPlayer.play()
        mediaPlayer.volume = 1.0
    }

    private fun build() {
        windows.initStyle(StageStyle.TRANSPARENT)

        windows.x = 100.0
        windows.y = 100.0
        val width = 300.0
        val height = 175.0

        windows.scene = Scene(root, width, height)
        windows.scene.fill = Color.TRANSPARENT

        root.style = "-fx-background-color: #c63c57"
        root.setPrefSize(width, height)
        root.padding = Insets(10.0, 10.0, 10.0, 10.0)

        setImage()

        val vbox = VBox()

        val title = Label(config.title)
        val message = Label(config.message)
        val appName = Label(config.appName)
        vbox.children.addAll(title, message, appName)
        box.children.add(vbox)
        root.center = box

        title.style = "-fx-font-style: oblique"
        message.style = "-fx-font-style: italic"
        appName.style = "-fx-font-style: normal"
    }

    private fun setImage() {
        if (config.image.isEmpty()) {
            return
        }

        val iconBorder = if (config.imageType == ImageStyle.CIRCLE) {
            Rectangle(150.0, 150.0)
        }
        else {
            Circle(75.0, 75.0, 75.0)
        }
        iconBorder.setFill(ImagePattern(Image(config.image)))
        box.children.add(iconBorder)
    }

    private fun openAnimation() {
//        val anim = TranslateTransition(Duration.millis(1500.0), root)
        val anim = FadeTransition(Duration.millis(1500.0), root)
//        val animation = if (config.animation == AnimationType.TRANSLATE) {
//            Translate(7.0)
//        }
        anim.fromValue = 0.0
        anim.toValue = config.alpha
        anim.cycleCount = 1
        anim.play()
    }
    private fun closeAnimation() {
        //val anim = TranslateTransition(Duration.millis(1500.0), root)
        val anim = FadeTransition(Duration.millis(1500.0), root)
        anim.fromValue = config.alpha
        anim.toValue = 0.0
        anim.cycleCount = 1
        anim.onFinished = EventHandler {
            Platform.exit()
            System.exit(0)
        }
        anim.play()
    }

    fun start() {
        windows.show()
        openAnimation();
        playNotify();
        val thread = Thread {
            try {
                Thread.sleep(config.openTime.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            closeAnimation()
        }
        Thread(thread).start()
    }

}
class SomeClass: Application() {
    override fun start(p0: Stage?) {
        var toast = Toast.Builder()
            .setTitle("T.N.T")
            .setMessage("Iron Man 2")
            .setAppName("AC/DC")
            .build()
        toast.start()
    }
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(SomeClass::class.java)
        }
    }
}