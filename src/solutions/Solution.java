package solutions;

import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public abstract class Solution {
    private final Client CLIENT = new Client(Protocol.HTTP);

    public abstract String normalize(String address);
    public abstract LatLong geocode(String address);
    public abstract String reverseGeocode(LatLong latlong);

    protected Representation getRepresentation(Reference ref) {
        ClientResource res = new ClientResource(ref);
        res.setNext(CLIENT);
        return res.get();
    }

    protected Representation getRepresentation(String url) {
        ClientResource res = new ClientResource(url);
        res.setNext(CLIENT);
        return res.get();
    }
}