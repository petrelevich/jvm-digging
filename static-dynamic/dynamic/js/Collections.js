const getArray = () => {
    const array = [1, 2, 3 , 4]
    array.push("elem1")
    array.push("elem2")
    array.push(new Date())
    array.push(new Date())
    array.pop()
    return array
}