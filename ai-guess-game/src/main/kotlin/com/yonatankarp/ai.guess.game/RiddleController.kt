package com.yonatankarp.ai.guess.game

import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import kotlin.random.Random

@Controller
class RiddleController(
    val riddleService: RiddleService,
    val riddleManager: RiddleManager
) {

    companion object {
        private val log = LoggerFactory.getLogger(RiddleController::class.java)

        private const val MAX_NUMBER_OF_RIDDLES = 3
    }

    @RequestMapping(value = ["/", "index", "index.html"])
    fun index(model: Model): String {
        val riddleIndex = Random.nextInt(from = 1, until = MAX_NUMBER_OF_RIDDLES + 1)
        log.info("Reading riddle id: $riddleIndex")
        model.addAttribute("guess", Guess("Enter a word..."))
        model.addAttribute("riddle", riddleManager.getRiddle(riddleIndex))
        return "index"
    }

    @GetMapping(value = ["/{id}"], produces = [MediaType.IMAGE_PNG_VALUE])
    @ResponseBody
    fun getImage(@PathVariable id: Int) = riddleService.getImage(id)

    @RequestMapping(value = ["/{id}"])
    fun getRiddle(@PathVariable id: Int, model: Model): String {
        model.addAttribute("riddle", riddleManager.getRiddle(id))
        return "index"
    }

    @PostMapping("/{id}/guess")
    fun submitGuess(@PathVariable id: Int, @ModelAttribute guess: Guess, model: Model): String {
        log.info("Guess for id $id is ${guess.phrase}")
        riddleService.handleGuess(id, guess)
            .let {
                model.addAttribute("riddle", riddleManager.getRiddle(id))
                model.addAttribute("guess", guess)
                model.addAttribute("results", it)
            }
        return "index"
    }
}