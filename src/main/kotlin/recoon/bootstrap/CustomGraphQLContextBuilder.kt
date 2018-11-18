package recoon.bootstrap

import graphql.servlet.DefaultGraphQLContextBuilder
import graphql.servlet.GraphQLContext
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.websocket.server.HandshakeRequest

@Component
class CustomGraphQLContextBuilder : DefaultGraphQLContextBuilder(){
	
    override fun build(httpServletRequest:HttpServletRequest ) : GraphQLContext{
        return CustomGraphQLContext(httpServletRequest);
    }

    override fun build(handshakeRequest:HandshakeRequest) : GraphQLContext {
        return CustomGraphQLContext(handshakeRequest);
    }

    override fun build(): GraphQLContext {
        return CustomGraphQLContext();
    }
	
}

class CustomGraphQLContext : GraphQLContext {
	constructor() : super()
	constructor(httpServletRequest:HttpServletRequest) : super(httpServletRequest)
	constructor(handshakeRequest:HandshakeRequest) : super(handshakeRequest)

	val resolvedValues : MutableMap<String, Any> = mutableMapOf()
	
	fun addResolvedValue(name:String, value: Any){
		resolvedValues.put(name, value)
	}
	
	fun getResolvedValue(name:String) = resolvedValues.get(name)
}