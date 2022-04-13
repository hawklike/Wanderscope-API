package cz.cvut.fit.steuejan.travel.api.trip.exception

import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages

open class ChangeRoleException(override val message: String, val code: Int) : Exception(message)

class CannotLeaveException(
    message: String = FailureMessages.LEAVE_TRIP_PROHIBITED,
    code: Int = 1
) : ChangeRoleException(message, code)

class CannotChangeRoleToMyselfException(
    message: String = FailureMessages.CHANGE_ROLE_TO_MYSELF_PROHIBITED,
    code: Int = 2
) : ChangeRoleException(message, code)

class CannotChangeRoleException(
    message: String = FailureMessages.EDIT_CHANGE_ROLE_PROHIBITED,
    code: Int = 3
) : ChangeRoleException(message, code)