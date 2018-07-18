using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Description;
using Web_APIC2CDataAccess;

namespace Web_APIC2C.Controllers
{
    public class UsersController : ApiController
    {
        public IEnumerable<User> Get()
        {
            using (C2CthesisEntities entities = new C2CthesisEntities())
            {
                return entities.Users.ToList();
            }
        }

        public String Get(String phone)
        {
            using (C2CthesisEntities entities = new C2CthesisEntities())
            {
                return entities.Users.FirstOrDefault(e => e.UserPhonenumber == phone).UserPoint.ToString();
            }
        }

        [ResponseType(typeof(User))]
        public async Task<IHttpActionResult> PostUser(User user)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            using (C2CthesisEntities entities = new C2CthesisEntities())
            {
                entities.Users.Add(user);
                await entities.SaveChangesAsync();
            }

            return CreatedAtRoute("DefaultApi", new { id = user.UserPhonenumber }, user);
        }
    }
}
