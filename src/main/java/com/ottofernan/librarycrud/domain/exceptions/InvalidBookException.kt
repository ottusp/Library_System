package com.ottofernan.librarycrud.domain.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class InvalidBookException(override val message: String) : RuntimeException(message) {

}