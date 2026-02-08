from pydantic import BaseModel

class IntegrationBase(BaseModel):
    service_name: str


class IntegrationCreate(IntegrationBase):
    api_token: str

class IntegrationOut(IntegrationBase):
    id: int 

    class Config:
        from_attributes = True  # Для Pydantic v2, совместимо с orm_mode
