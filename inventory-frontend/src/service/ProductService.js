export class ProductService {


    getProducts() {
        return fetch(`./assets/demo/data/products.json`).then(res => res.json()).then(d => d.data);
    }

}