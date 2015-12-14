/**
 * Created by eboh on 16/11/15.
 */
var ProductList = React.createClass({
    getInitialState: function () {
        return {
            list: [],
            filter: {},
            shoppingList: []
        }
    },

    addToShoppingList: function(item){
        console.log("adding "+item.namn+" to shopping list, in parent");
        this.setState({shoppingList: shoppingList.concat([item.namn])});
    },

    componentDidMount: function () {
        $.get(this.props.source, function (result) {
            var products = result._embedded.artiklar;
            if (this.isMounted()) {
                this.setState({
                    list: products
                });
            }
        }.bind(this));
    },

    filterState: function (key, value) {
        var newFilter = {key: key, value: value};
        var currentFilters = this.state.filter;

        currentFilters[key] = value;

        this.setState({filter: currentFilters});
        console.log("key " + key + " value " + value);
    },

    render: function () {
        var list = this.state.list.filter(function (item) {
            var filters = this.state.filter;
            if (!Object.keys(filters).length) return true;

            return Object.keys(filters).map(function (key) {
                return item[key].indexOf(filters[key]) !== -1 || filters[key] === "No selection";
            }).reduce(function (acc, value) {
                return acc && value;
            }, true);

        }, this).map(function (item) {
            return (
                <Product item={item} onClick={this.addToShoppingList}/>
            );
        }.bind(this));

        console.log("Number of items: " + list.length);

        return (
            <div>
                <ShoppingList shoppingList={this.state.shoppingList} />
                <HeaderSelectComponent source="http://localhost:8080/meta/categories" attribute="varugrupp"
                                       onChange={this.filterState}/><br/>
                <HeaderSelectComponent source="http://localhost:8080/meta/originCountry" attribute="ursprunglandnamn"
                                       onChange={this.filterState}/><br/>
                <HeaderSelectComponent source="http://localhost:8080/meta/originYear" attribute="argang"
                                       onChange={this.filterState}/><br/>
                <HeaderFreeTextComponent attribute="namn" onChange={this.filterState}/><br/>
                <ul>
                    {list}
                </ul>
            </div>
        );
    }

});

var ShoppingList = React.createClass({
    render: function () {
        var list = this.props.shoppingList.map(function (item) {
            return (
                <div>
                    {item.namn}
                </div>
            );
        });
        return (<div>{list}</div>);
    }
});

var Product = React.createClass({
    getInitialState: function(){
        return {}
    },
    addToShoppinglist: function(item){
        console.log("adding "+item.namn+" to shopping list, in child");
        this.props.onClick(item);
    },

    render: function () {
        return (
            <li>
                {this.props.item.namn} -
                {this.props.item.prisinklmoms} - <input type="button" onClick={this.addToShoppinglist(this.props.item)}>Add to list</input>
            </li>
        );
    }
});

var HeaderFreeTextComponent = React.createClass({
    getInitialState: function () {
        return {
            filterText: ""
        }
    },

    handleChange: function () {
        this.props.onChange(this.props.attribute, this.refs.filterTextField.value);
    },

    render: function () {
        return (<input type="text" ref="filterTextField" placeholder="filter..." onChange={this.handleChange}/> );
    }
});

var HeaderSelectComponent = React.createClass({
    getInitialState: function () {
        return {
            options: []
        }
    },

    componentDidMount: function () {
        $.get(this.props.source, function (result) {
            if (this.isMounted()) {
                var options = ["No selection"].concat(result);
                this.setState({options: options});
            }
        }.bind(this));
    },

    handleChange: function (event) {
        console.log(event.target.value);
        this.props.onChange(this.props.attribute, event.target.value);
    },

    render: function () {
        var selectOptions = this.state.options.map(function (item) {
            return (
                <option key={item} value={item}>{item}</option>
            );
        });
        return (
            <select onChange={this.handleChange}>
                {selectOptions}
            </select>
        )
    }

});

ReactDOM.render(<ProductList source="http://localhost:8080/artiklar?page=0&size=100000"/>,
    document.getElementById('content'));