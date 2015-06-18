package mytodo

import javax.xml.bind.DatatypeConverter

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

//@Transactional(readOnly = true)
class TodoController {

    //static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    //static scaffold = true
    //save, delete, edit, show all, show active, show close, find by title

    def index() {
        println "index action called"
        def map = [todo: Todo.get(params.id)]
        render "Hello index! ${map}"
    }

    def listxml() {
        def results = Todo.list()

        render(contentType: "text/xml") {
            todos {
                for (t in results) {
                    todo(title: t.title)
                }
            }
        }
    }

    def listjson() {
        def results = Todo.list()

        render(contentType: "application/json") {
            todos = array {
                for (t in results) {
                    todo title: t.title
                }
            }
        }
    }

    def display() {
        println "display action called"
        //render "Hello display! ${Todo.list()}"
        redirect(action: 'show')
    }

    def show(long id) {
        println "show action called with id ${id}"
        render "Hello show! ${id} - ${Todo.list()}"
    }

    @Transactional
    def save() {
        println "save action called ${params}"

        try {
            def t = new Todo()
            //t.properties = params
            t.title = params.title
            t.is_active = params.boolean("is_active")
            t.due_date = Date.parse("yyyy-MM-dd", params.due_date)

            println "before save ${t}"

            t.save(flush: true)
            render "Hello save! ${t}"
        }
        catch (org.springframework.dao.DataIntegrityViolationException e) {
            // deal with exception
        }
    }

    @Transactional
    def delete() {
        println "delete action called ${params}"
        def t = Todo.get(params.id)
        try {
            t.delete(flush: true)
            render "Todo ${t.id} was deleted"
        }
        catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = "Could not delete todo ${t.name}"
            redirect(action: "show", id: t.id)
        }
    }

    @Transactional
    def edit() {
        println "edit action called ${params}"

        try {
            def t = Todo.get(params.id)

            if(!t)
            {
                println "not found"
                return;
            }

            t.title = params.title
            t.is_active = params.boolean("is_active")
            t.due_date = Date.parse("yyyy-MM-dd", params.due_date)

            t.save(flush: true)
            render "Hello edit! ${t}"
        }
        catch (org.springframework.dao.DataIntegrityViolationException e) {
            // deal with exception
        }
    }

    def findByTitle() {
        println "findByTitle action called ${params}"
        def todos = Todo.findByTitle(params.title)
        render "Todo ${todos}"
    }

    def findAllActive() {
        def todos = Todo.findAll {
            is_active == true
        }

        render "Todo ${todos}"
    }

    def findAllNonActive() {
        def todos = Todo.findAll {
            is_active == false
        }

        render "Todo ${todos}"
    }
/*

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Todo.list(params), model:[todoInstanceCount: Todo.count()]
    }

    def show(Todo todoInstance) {
        respond todoInstance
    }

    def create() {
        respond new Todo(params)
    }

    @Transactional
    def save(Todo todoInstance) {
        if (todoInstance == null) {
            notFound()
            return
        }

        if (todoInstance.hasErrors()) {
            respond todoInstance.errors, view:'create'
            return
        }

        todoInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'todo.label', default: 'Todo'), todoInstance.id])
                redirect todoInstance
            }
            '*' { respond todoInstance, [status: CREATED] }
        }
    }

    def edit(Todo todoInstance) {
        respond todoInstance
    }

    @Transactional
    def update(Todo todoInstance) {
        if (todoInstance == null) {
            notFound()
            return
        }

        if (todoInstance.hasErrors()) {
            respond todoInstance.errors, view:'edit'
            return
        }

        todoInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Todo.label', default: 'Todo'), todoInstance.id])
                redirect todoInstance
            }
            '*'{ respond todoInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Todo todoInstance) {

        if (todoInstance == null) {
            notFound()
            return
        }

        todoInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Todo.label', default: 'Todo'), todoInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'todo.label', default: 'Todo'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
*/
}
