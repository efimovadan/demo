package com.example.demo

import javafx.animation.FadeTransition
import javafx.animation.TranslateTransition
import javafx.application.Application
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.*
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import java.io.File

enum class ImageStyle {
    CIRCLE,
    RECTANGLE
}

enum class AnimationType {
    FADE_TRANSITION,
    TRANSLATE_TRANSITION
}

enum class Position {
    RIGHT_TOP,
    RIGHT_BOTTOM,
    LEFT_TOP,
    LEFT_BOTTOM
}
enum class ButtonsQuantity {
    ZERO,
    ONE,
    TWO
}

enum class ButtonType {
    CLOSE,
    REPLY
}

class Config {
    val position = Position.RIGHT_BOTTOM
    var alpha = 0.9
    var openTime = 7000.0
    var imageType = ImageStyle.RECTANGLE
    var animation = AnimationType.TRANSLATE_TRANSITION
    var title = "TITLE"
    var message = "MESSAGE"
    var appName = "APP NAME"
    var image = "https://sun9-62.userapi.com/impg/SXUIhxiCQfsZMBSyBCpXPY9gTSPTHDj631KozA/nw1OZ03sAIc.jpg?size=315x270&quality=96&sign=d0927f953e46ef8474402006bc70837d&type=album"
    var soundEffect = "C:\\Users\\pen\\IdeaProjects\\laba\\demo\\src\\main\\resources\\new_message_tone.wav"
    var buttonsQuantity = ButtonsQuantity.TWO
    var firstButtonType = ButtonType.CLOSE
    var secondButtonType = ButtonType.REPLY
}

class Toast {
    private var config = Config()
    private val windows = Stage()
    private var root = BorderPane()
    private var box = HBox()
    private var primaryScreenBounds = Screen.getPrimary().visualBounds
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

        fun build(): Toast {
            var toast = Toast()
            toast.config = config
            toast.build()

            return toast
        }
    }


    private fun build() {
        val vbox = VBox()
        val title = Label(config.title)
        val message = Label(config.message)
        val appName = Label(config.appName)

        windows.initStyle(StageStyle.TRANSPARENT)
        windows.scene = Scene(root)
        windows.width = 350.0
        windows.scene.fill = Color.TRANSPARENT

        root.style = "-fx-margin: 10px; -fx-padding: 10px; -fx-background-color: #b5b7b6;"
        root.prefWidth = windows.width
        root.padding = Insets(10.0, 10.0, 10.0, 10.0)

        title.maxWidth = 200.0
        message.maxWidth = 200.0
        appName.maxWidth = 200.0

        title.isWrapText = true
        message.isWrapText = true
        appName.isWrapText = true

        title.style = "-fx-font-family: Segoe UI; -fx-font-weight: BOLD; -fx-font-size: 20px"
        message.style = "-fx-font-family: Segoe UI; -fx-font-size: 18px;"
        appName.style = "-fx-font-family: Segoe UI; -fx-font-size: 18px;"

        setImage()

        vbox.children.addAll(title, message, appName)
        box.spacing = 7.0
        box.children.add(vbox)
        root.center = box

        if (config.buttonsQuantity != ButtonsQuantity.ZERO) {
            addButtons()
        }
    }

    private fun setImage() {
        if (config.image.isEmpty()) {
            return
        }

        val iconBorder = if (config.imageType == ImageStyle.RECTANGLE) {
            Rectangle(100.0, 100.0)
        } else {
            Circle(50.0, 50.0, 50.0)
        }
        iconBorder.setFill(ImagePattern(Image(config.image)))
        box.children.add(iconBorder)
    }

    private fun replyButtonEvent() {
        val reply = BorderPane()
        val textField = TextField("Reply")
        val sendButton = Button("Send")
        val eventSendButton = EventHandler<ActionEvent>() {closeAnimation(reply)}

        sendButton.style = "-fx-background-color: #2c2e34; -fx-font-size: 18px; -fx-font-family: Segoe UI; -fx-background-radius: 0px; -fx-text-fill: #ffffff;"
        sendButton.maxWidth = Double.MAX_VALUE
        sendButton.onAction = eventSendButton
        HBox.setHgrow(sendButton, Priority.ALWAYS)

        textField.style = "-fx-border-color: black; -fx-font-size: 18px"

        reply.style = "-fx-padding: 15px; -fx-margin: 10px; -fx-background-color: #b5b7b6;"
        reply.center = textField
        reply.bottom = sendButton

        windows.scene = Scene(reply, windows.width, windows.height)
        windows.scene.fill = Color.TRANSPARENT

    }

    private fun closeButton(): Button {
        val button = Button("Close")
        val eventCloseButton = EventHandler<ActionEvent>() {closeAnimation(root)}
        button.onAction = eventCloseButton
        button.style = "-fx-background-color: #2c2e34; -fx-font-size: 18px; -fx-background-radius: 0px; -fx-font-family: Segoe UI; -fx-text-fill: #ffffff;"
        button.maxWidth = Double.MAX_VALUE
        HBox.setHgrow(button, Priority.ALWAYS)

        return button
    }

    private fun replyButton(): Button {
        val button = Button("Reply")
        val eventReplyButton = EventHandler<ActionEvent>() {replyButtonEvent()}
        button.onAction = eventReplyButton
        button.style = "-fx-background-color: #2c2e34; -fx-font-size: 18px; -fx-background-radius: 0px; -fx-font-family: Segoe UI; -fx-text-fill: #ffffff;"
        button.maxWidth = Double.MAX_VALUE
        HBox.setHgrow(button, Priority.ALWAYS)

        return button
    }

    private fun addButtons() {
        var buttons = HBox()
        when (config.buttonsQuantity) {
            ButtonsQuantity.ONE -> {
                when (config.firstButtonType) {
                    ButtonType.CLOSE -> buttons.children.add(closeButton())
                    else -> buttons.children.add(replyButton())
                }
            }

            else -> {
                when (config.firstButtonType) {
                    ButtonType.CLOSE -> buttons.children.add(closeButton())
                    else -> buttons.children.add(replyButton())
                }

                when (config.secondButtonType) {
                    ButtonType.CLOSE -> buttons.children.add(closeButton())
                    else -> buttons.children.add(replyButton())
                }
            }
        }

        buttons.spacing = 7.0
        root.bottom = buttons
    }

    private fun playNotify() {
        val media = Media(File(config.soundEffect).toURI().toString())
        val player = MediaPlayer(media)
        player.play()
    }


    private fun getCoords() {
        when (config.position) {
            Position.LEFT_TOP -> {
                windows.x = 0.0
                windows.y = 0.0
            }

            Position.LEFT_BOTTOM -> {
                windows.x = 0.0
                windows.y = primaryScreenBounds.height - windows.height
            }

            Position.RIGHT_TOP -> {
                windows.x = primaryScreenBounds.width - windows.width
                windows.y = 0.0
            }

            else -> {
                windows.x = primaryScreenBounds.width - windows.width
                windows.y = primaryScreenBounds.height - windows.height
            }
        }
    }

    private fun openAnimation() {
        getCoords()
        playNotify()
        when (config.animation) {
            AnimationType.FADE_TRANSITION -> {
                val anim = FadeTransition(Duration.millis(1500.0), root)
                anim.fromValue = 0.0
                anim.toValue = config.alpha
                anim.cycleCount = 1
                anim.play()
            }
            else -> {
                val anim = TranslateTransition(Duration.millis(1500.0), root)
                if ((config.position == Position.LEFT_TOP) or (config.position == Position.LEFT_BOTTOM)) {
                    anim.fromX = windows.x - windows.width
                    anim.toX = windows.x
                } else {
                    anim.fromX = 0.0 + windows.width
                    anim.toX = 0.0
                }
                anim.cycleCount = 1
                anim.play()
            }
        }
    }

    private fun closeAnimation(root: Node) {
        when (config.animation) {
            AnimationType.FADE_TRANSITION -> {
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
            else -> {
                val anim = TranslateTransition(Duration.millis(1500.0), root)
                if ((config.position == Position.LEFT_TOP) or (config.position == Position.LEFT_BOTTOM)) {
                    anim.fromX = windows.x
                    anim.toX = windows.x - windows.width
                } else {
                    anim.fromX = 0.0
                    anim.toX = 0.0 + windows.width
                }
                anim.cycleCount = 1
                anim.onFinished = EventHandler {
                    Platform.exit()
                    System.exit(0)
                }
                anim.play()
            }
        }
    }

    fun start() {
        windows.show()
        openAnimation()
        val thread = Thread {
            try {
                Thread.sleep(config.openTime.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            closeAnimation(root)
        }
        Thread(thread).start()
    }

}


class SomeClass : Application() {
    override fun start(p0: Stage?) {
        var toast = Toast.Builder()
            .setTitle("NEW NOTIFICATION")
            .setMessage("Message")
            .setAppName("VK")
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