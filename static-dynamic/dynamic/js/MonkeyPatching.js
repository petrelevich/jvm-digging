const OriginalObj = {
    intValue: 'someVal',

    getIntValue: function () {
        return this.intValue
    }
}

const beforePatching = () => {
    const originalObj = Object.create(OriginalObj)
    return originalObj.getIntValue()
}

const afterPatching = () => {
    OriginalObj.getIntValue = function () {
            return this.intValue.toUpperCase()
    }

    const patchedObj = Object.create(OriginalObj)
    return patchedObj.getIntValue()
}

const afterPatchingUpperCase = () => {
    String.prototype.toUpperCase = function() {
        return "something"
    }

    const patchedObj = Object.create(OriginalObj)
    return patchedObj.getIntValue()
}