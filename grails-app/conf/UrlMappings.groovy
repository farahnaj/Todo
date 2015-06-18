class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        //"/todos"(resources:'todo')
        "/"(view:"/index")
        "500"(view:'/error')
	}
}
