// Create comment interface, which includes a text entry bar, with buttons "Send", and button "Cancel".

function CommentForm(){
    return(
        <div>
            <form>
                <label htmlFor="comment">Comment: </label>
                <textarea id="comment" name="comment"></textarea>
                <button type="submit">Send</button>
                <button type="reset">Cancel</button>
            </form>
        </div>
    )
}

export default CommentForm;