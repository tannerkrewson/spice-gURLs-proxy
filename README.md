# spice-gUrls-proxy

In this project you'd be designing and developing a web proxy server. 

A web proxy delivers content on behalf of remote web servers. In this project you would develop a multithreaded web proxy that supports a subset of functionalities as per RFC 2616. 

Client -> Web Proxy -> Web Server (Request)

Client <- Web Proxy <- Web Server (Response)

Your proxy server receives request from the client, forwards it to the web server and receives a response. It then sends this response back to the Client. 

A proxy server also performs caching along with being a forwarding agent. Due to caching the proxy server stores the fetched content in the local disk and provides the client this local copy in response to a request. The server however periodically checks for the newer version with the web server and if detects the change then retrieves the latest copy. 

You are required to distinguish and design and develop the server accordingly for: a) HTTP GET, b)  If-Modified-Since, c) HTTP response codes 304, 400 & 501.

For validation, you could use browser or terminal (wget) to demonstrate that your requests are going to the proxy server and the response is also getting from the same server. In addition, for testing/validation purposes you could create a simple website to demonstrate how your tool would work or you could use any other website as well.

What to submit: A pdf file containing your project report with entire source code (adequately commented) for your proxy server. A description of your multithreaded server design. Each team member's contributions. Any other additional information you would like to add. By multithreaded server it means that it should be able to serve multiple requests in parallel coming from same or different clients.

Points: 10 points (project report) + 5 points (validation).



TODO:
Get modified time from google (in get response from url method)
Get a Cache object up and running
Handle 304 
- Indicates that the resource has not been modified since the version specified by the request headers If-Modified-Since or If-None-Match. In such case, there is no need to retransmit the resource since the client still has a previously-downloaded copy.[26]
400
- malformed request syntax, size too large, invalid request message framing, or deceptive request routing
- we'll focus on the syntax, I think. The other stuff might not be easy to measure. 
and 501
- request method is not supported by the server and cannot be handled. The only methods that servers are required to support (and therefore that must not return this code) are GET and HEAD.

